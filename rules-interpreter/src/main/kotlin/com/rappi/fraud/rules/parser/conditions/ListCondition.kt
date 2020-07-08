package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.Visitor
import com.rappi.fraud.rules.parser.removeSingleQuote

class ListCondition : Condition<ANAParser.ListContext> {

    override fun eval(ctx: ANAParser.ListContext, evaluator: Visitor): Any {
        return when (ctx.op.type) {
            ANALexer.K_CONTAINS -> evalContains(ctx, evaluator)
            ANALexer.K_IN -> evalIn(ctx, evaluator)
            else -> throw RuntimeException("Unexpected token near ${ctx.value.text}")
        }
    }

    private fun evalIn(ctx: ANAParser.ListContext, visitor: Visitor): Any {
        val value = visitor.visit(ctx.value)
        return when
        {
            (ctx.values.literalList != null) -> {
                val list = ctx.values.literalList.text.removeSingleQuote().split(",")
                list.contains(value as String?)
            }
            ctx.values.storedList != null -> {
                val value = value.toString()
                // TODO: STRING REPLACE MUST BE DONE IN LANGUAGE LEVEL USING STRING LITERAL
                val list = visitor.lists[ctx.values.STRING_LITERAL()[0].toString().replace("\'", "")]

                list?.any {
                    value.contains(it, true)
                } ?: false
            }
            ctx.values.validProperty().property != null -> (visitor.data[ctx.values.validProperty().property.text] as List<*>).contains(
                value
            )
            ctx.values.validProperty().nestedProperty != null ->
                getFromProperty(ctx.values.validProperty(), visitor.data).contains(value)

            else -> error("Cannot find symbol")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun evalContains(ctx: ANAParser.ListContext, visitor: Visitor): Any {
       return when {
            ctx.values.literalList != null  -> {
                val value = visitor.visit(ctx.value)
                val list = ctx.values.text.removeSingleQuote().split(",")
                list.any {
                    value.toString().contains(it, true)
                }
            }
            ctx.values.storedList != null -> {
                val value = visitor.visit(ctx.value)
                // TODO: STRING REPLACE MUST BE DONE IN LANGUAGE LEVEL USING STRING LITERAL
                val list = visitor.lists[ctx.values.STRING_LITERAL()[0].toString().replace("\'", "")]

                list?.any {
                    value.toString().contains(it, true)
                } ?: false
                }
            else -> error("Cannot find symbol")
            }
        }



    @Suppress("UNCHECKED_CAST")
    fun getFromProperty(ctx: ANAParser.ValidPropertyContext, data: Map<String, *>): List<Any?> {
        var r = data
        ctx.ID().forEach { id ->
            when {
                r[id.text] is List<Any?> -> return r[id.text] as List<Any?>
                r[id.text] is Map<*, *> -> r = r[id.text] as Map<String, Any?>
                else -> return emptyList()
            }
        }
        return listOf()
    }
}