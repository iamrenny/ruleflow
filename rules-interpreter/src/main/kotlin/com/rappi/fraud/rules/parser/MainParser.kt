package com.rappi.fraud.rules.parser

import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import javafx.util.converter.BigIntegerStringConverter
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.math.RoundingMode


class RuleEngine(workflow: String)  {

    private val input : CodePointCharStream
    private val lexer : ANALexer
    private val tokens : CommonTokenStream
    private val parser : ANAParser
    private val tree : ANAParser.ParseContext
    private val walker : ParseTreeWalker
    private val listener : ANABaseListener

    init {
        input = CharStreams.fromString(workflow)
        lexer = ANALexer(input)
        tokens = CommonTokenStream(lexer)
        parser = ANAParser(tokens)
        parser.addErrorListener(ErrorListener())
        tree = parser.parse()
        walker = ParseTreeWalker()
        listener = ANABaseListener()
        walker.walk(listener, tree)
    }
    fun evaluate(data: Map<String, Any>): String {
        return EvaluateRulesetVisitor(data).visit(tree)
    }
}


class ErrorListener() : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        throw RuntimeException("error at line $line:$charPositionInLine - $msg")
    }
}

class EvaluateRulesetVisitor(val data: Map<String, Any>) : ANABaseVisitor<String>() {

    override fun visitParse(ctx: ANAParser.ParseContext): String {
        return visitWorkflow(ctx.workflow())
    }

    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): String {
        ctx.stmt_list().stmt().forEach {
            if (EvaluateConditionVisitor().visitCond(it.cond())) {
                val result = it.result_value().text.replace("'", "")
                return result
            }
        }

        return "default_${ctx.stmt_list().default_stmt().result_value().text.replace("'", "")}"
    }

    private inner class EvaluateDeepIdVisitor : ANABaseVisitor<Any>() {
        override fun visitDeep_id(ctx: ANAParser.Deep_idContext?): Any? {
            return when {
                ctx == null -> null
                else -> visitDeepIdRecursive(data, ctx)
            }
        }

        fun visitDeepIdRecursive(data: Map<String, *>?, ctx: ANAParser.Deep_idContext) : Any? {
            val queueOfDeepIds = mutableListOf(ctx)
            ctx.deep_id().forEach {
                queueOfDeepIds.add(it)
            }
            var r = (data as MutableMap)
            queueOfDeepIds.take(ctx.deep_id().size).forEach {
                if (r[it.ID().text] is Map<*, *>) {
                    r = r[it.ID().text] as MutableMap<String, out Any?>
                } else {
                    throw RuntimeException("${it.parent.text} is an invalid operation")
                }
            }
            return r[queueOfDeepIds.last().ID().text]
        }
    }

    private inner class EvaluateConditionVisitor : ANABaseVisitor<Boolean>() {

        override fun visitList_op(ctx: ANAParser.List_opContext?): Boolean {
            val list = EvaluateDeepIdVisitor().visitDeep_id(ctx!!.deep_id())
            return if( list is Collection<*>) {
                when {
                    ctx.K_ALL() != null -> list.all { _visitCond(ctx.cond(), it as Map<String, *>) }
                    ctx.K_ANY() != null -> list.any { _visitCond(ctx.cond(), it as Map<String, *>) }
                    else -> throw RuntimeException("Unexpected token near ${ctx.deep_id().text}")
                }
            } else {
                throw RuntimeException("${ctx.deep_id().text} is not a Collection")
            }
        }

        override fun visitCount(ctx: ANAParser.CountContext?): Boolean {
            val list = EvaluateDeepIdVisitor().visitDeep_id(ctx!!.deep_id())
            return if(list is Collection<*>) {
                val count = list.filter { _visitCond(ctx.cond(), it as Map<String, *>) }.size
                when {
                    ctx.GT() != null -> count.toLong() > ctx.NUMERIC_LITERAL().text.toLong()
                    ctx.GT_EQ() != null -> count.toLong() >= ctx.NUMERIC_LITERAL().text.toLong()
                    ctx.LT() != null -> count.toLong() < ctx.NUMERIC_LITERAL().text.toLong()
                    ctx.LT_EQ() != null -> count.toLong() <= ctx.NUMERIC_LITERAL().text.toLong()
                    else -> throw RuntimeException("Invalid Operation ${ctx.text}")
                }
            } else {
                throw RuntimeException("${ctx.deep_id().text} is not a Collection")
            }
        }

        override fun visitAverage(ctx: ANAParser.AverageContext?): Boolean {
            val list = EvaluateDeepIdVisitor().visitDeep_id(ctx!!.deep_id())
            return if(list is Collection<*>) {
                val count = list.filter { _visitCond(ctx.cond(), it as Map<String, *>) }.size.toBigDecimal()
                val average = list.size.toBigDecimal().divide(count, 3, RoundingMode.DOWN)
                when {
                    ctx.GT() != null -> average > ctx.NUMERIC_LITERAL().text.toBigDecimal()
                    ctx.GT_EQ() != null -> average >= ctx.NUMERIC_LITERAL().text.toBigDecimal()
                    ctx.LT() != null -> average < ctx.NUMERIC_LITERAL().text.toBigDecimal()
                    ctx.LT_EQ() != null -> average <= ctx.NUMERIC_LITERAL().text.toBigDecimal()
                    else -> throw RuntimeException("Invalid Operation ${ctx.text}")
                }
            } else {
                throw RuntimeException("${ctx.deep_id().text} is not a Collection")
            }
        }

        override fun visitCond(ctx: ANAParser.CondContext?): Boolean {
            return if(ctx != null) {
                 _visitCond(ctx , data)
            } else {
                throw RuntimeException("Invalid Context")
            }
        }

        fun _visitCond(ctx: ANAParser.CondContext, data: Map<String,*>): Boolean {
            if (ctx.list_op() != null) {
                return visitList_op(ctx.list_op())
            }
            if (ctx.count() != null) {
                return visitCount(ctx.count())
            }
            if (ctx.average() != null) {
                return visitAverage(ctx.average())
            }
            if (ctx.deep_id() != null) {
                val ruleCtx = EvaluateDeepIdVisitor().visitDeepIdRecursive(data, ctx.deep_id())
                ruleCtx?: throw RuntimeException("Invalid Operation ${ctx.deep_id().text}")
                when {
                    ctx.EQ() != null ->
                        return ruleCtx == ctx.STRING_LITERAL().text.replace("'", "")
                    ctx.NOT_EQ1() != null ->
                        return ruleCtx != ctx.STRING_LITERAL().text.replace("'", "")
                    ctx.LT() != null ->
                        return BigIntegerStringConverter().fromString(ruleCtx.toString()).toLong() < ctx.NUMERIC_LITERAL().text.toLong()
                    ctx.GT() != null ->
                        return BigIntegerStringConverter().fromString(ruleCtx.toString()).toLong() > ctx.NUMERIC_LITERAL().text.toLong()
                }
            }

            if (ctx.cond() != null) {
                when {
                    ctx.K_AND() != null -> return visitCond(ctx.cond(0)) && visitCond(ctx.cond(1))
                    ctx.K_OR() != null -> return visitCond(ctx.cond(0)) || visitCond(ctx.cond(1))
                }
            }
            return false
        }
    }
}

class EvaluateWorkflow : ANABaseListener() {
    override fun enterWorkflow(ctx: ANAParser.WorkflowContext) {
        println("Setting workflow name as: ${ctx.STRING_LITERAL(0)}")
        println("Setting ruleset name as: ${ctx.STRING_LITERAL(1)}")

    }


    override fun enterCond(ctx: ANAParser.CondContext) {
        println("expr is ${ctx.text}")
    }
}