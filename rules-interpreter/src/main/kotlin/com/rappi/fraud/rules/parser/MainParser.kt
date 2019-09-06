package com.rappi.fraud.rules.parser

import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.sun.xml.internal.bind.v2.model.core.ID
import javafx.util.converter.BigIntegerStringConverter
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.lang.RuntimeException
import java.util.*


class RuleEngine(private val workflow: String)  {
    private val input = CharStreams.fromString(workflow)
    private val lexer = ANALexer(input)
    private val tokens = CommonTokenStream(lexer)
    private val parser = ANAParser(tokens)
    private val tree = parser.parse()
    private val walker = ParseTreeWalker()
    private val listener = ANABaseListener()
    init {
        walker.walk(listener, tree)
    }
    fun evaluate(data: Map<String, Any>): String {
        return EvaluateRulesetVisitor(data).visit(tree)
    }
}


class EvaluateRulesetVisitor(val data: Map<String, Any>) : ANABaseVisitor<String>() {
    override fun visitParse(ctx: ANAParser.ParseContext): String {
        return visitWorkflow(ctx.workflow())
    }

    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): String {
        ctx.stmt_list().stmt().forEach {
            if (EvaluateConditionVisitor().visitCond(it.cond())) return it.result_value().text.replace("'", "")
        }

        return ctx.stmt_list().default_stmt().result_value().text.replace("'", "")
    }

    private inner class EvaluateConditionVisitor : ANABaseVisitor<Any>() {

        var ruleData: Any? = null
        override fun visitList_op(ctx: ANAParser.List_opContext?): Boolean {
            return visitCond(ctx!!.cond())
        }

        override fun visitDeep_id(ctx: ANAParser.Deep_idContext?): Any? {
            return when {
                ctx == null -> null
                else -> visitDeepIdRecursive(data, ctx,0)
            }
        }

        private fun visitDeepIdRecursive(data: Map<String, *>?, ctx: ANAParser.Deep_idContext, i: Int) : Any? {
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

        override fun visitCond(ctx: ANAParser.CondContext): Boolean {
            println(ctx.text)
            if (ctx.list_op() != null) {
                return visitList_op(ctx.list_op())
            }
            if (ctx.deep_id() != null) {
                val ruleCtx = visitDeep_id(ctx.deep_id())
                if (ruleCtx == null) return false // no risk
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