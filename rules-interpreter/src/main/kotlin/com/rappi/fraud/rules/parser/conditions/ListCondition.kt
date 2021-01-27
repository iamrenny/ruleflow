package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.Visitor
import com.rappi.fraud.rules.parser.removeSingleQuote
import org.antlr.v4.runtime.tree.TerminalNode

class ListCondition : Condition<ANAParser.ListContext> {

    override fun eval(ctx: ANAParser.ListContext, evaluator: Visitor): Any {

        return when {
            ctx.not == null && ctx.op.type == ANALexer.K_CONTAINS -> evalContains(ctx, evaluator)
            ctx.not != null && ctx.op.type == ANALexer.K_CONTAINS -> evalContains(ctx, evaluator) == false
            ctx.not == null && ctx.op.type == ANALexer.K_IN -> evalIn(ctx, evaluator)
            ctx.not != null && ctx.op.type == ANALexer.K_IN -> evalIn(ctx, evaluator) == false
            ctx.not == null && ctx.op.type == ANALexer.K_STARTS_WITH -> evalStartsWith(ctx, evaluator)
            ctx.not != null && ctx.op.type == ANALexer.K_STARTS_WITH -> evalStartsWith(ctx, evaluator) == false
            else -> throw RuntimeException("Unexpected token near ${ctx.value.text}")
        }

    }


    private fun evalIn(ctx: ANAParser.ListContext, visitor: Visitor): Any {
        val value = visitor.visit(ctx.value)
        return when {
            (ctx.values.literalList != null) -> {
                ctx.values.string_literal()
                    .map{it.text.replace("'", "", true)}
                    .map{ it.replace("'", "") }
                    .contains(value)
            }
            ctx.values.storedList != null -> {
                val value = value.toString()
                // TODO: STRING REPLACE MUST BE DONE IN LANGUAGE LEVEL USING STRING LITERAL
                val list = visitor.lists[ctx.values.string_literal()[0].text.replace("\'", "")]

                list?.contains(value) ?: false
            }
            ctx.values.validProperty() != null && ctx.values.validProperty().property != null ->
                (visitor.data[ctx.values.validProperty().property.text] as List<*>)
                    .contains(
                        value
                    )
            ctx.values.validProperty() != null && ctx.values.validProperty().nestedProperty != null ->
                (visitor.visit(ctx.values.validProperty()) as List<*>)
                    .contains(value)

            else -> error("Cannot find symbol ${ctx.values}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun evalContains(ctx: ANAParser.ListContext, visitor: Visitor): Any {
       return when {
            ctx.values.literalList != null  -> {
                val value = visitor.visit(ctx.value)
                val list = ctx.values.string_literal()
                list.any {
                    value.toString().contains(it.text.replace("'", ""), true)
                }
            }
            ctx.values.storedList != null -> {
                val value = visitor.visit(ctx.value)
                // TODO: STRING REPLACE MUST BE DONE IN LANGUAGE LEVEL USING STRING LITERAL
                val list = visitor.lists[ctx.values.string_literal()[0].text.replace("\'", "")]

                list?.any {
                    value.toString().contains(it, true)
                } ?: false
                }
            else -> error("Cannot find symbol")
            }
        }

    private fun evalStartsWith(ctx: ANAParser.ListContext, visitor: Visitor): Any {
        val value = visitor.visit(ctx.value).toString()
        val list =  when {
            (ctx.values.literalList != null) -> {
                ctx.values.string_literal()
                    .map{it.text.replace("'", "", true)}
            }
            ctx.values.storedList != null -> {
                // TODO: STRING REPLACE MUST BE DONE IN LANGUAGE LEVEL USING STRING LITERAL
               visitor.lists[ctx.values.string_literal()[0].text.replace("\'", "")]
            }
            ctx.values.validProperty() != null && ctx.values.validProperty().property != null -> (visitor.data[ctx.values.validProperty().property.text] as List<*>)

            ctx.values.validProperty() != null && ctx.values.validProperty().nestedProperty != null ->
                (visitor.visit(ctx.values.validProperty()) as List<*>)
            else -> error("Unexpected symbol ${ctx.values}")
        }
        return list?.map { elem -> value.startsWith(elem.toString(), true) }
            ?.reduce { fst, snd -> fst || snd } ?: false
    }


}