package com.rappi.analang.evaluators

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.analang.visitors.Visitor


class ListContextEvaluator : ContextEvaluator<ANAParser.ListContext> {

    override fun evaluate(ctx: ANAParser.ListContext, visitor: Visitor): Any {

        return when {
            ctx.not == null && ctx.op.type == ANALexer.K_CONTAINS -> evalContains(ctx, visitor)
            ctx.not != null && ctx.op.type == ANALexer.K_CONTAINS -> evalContains(ctx, visitor) == false
            ctx.not == null && ctx.op.type == ANALexer.K_IN -> evalIn(ctx, visitor)
            ctx.not != null && ctx.op.type == ANALexer.K_IN -> evalIn(ctx, visitor) == false
            ctx.not == null && ctx.op.type == ANALexer.K_STARTS_WITH -> evalStartsWith(ctx, visitor)
            ctx.not != null && ctx.op.type == ANALexer.K_STARTS_WITH -> evalStartsWith(ctx, visitor) == false
            else -> throw RuntimeException("Unexpected token near ${ctx.value.text}")
        }

    }


    private fun evalIn(ctx: ANAParser.ListContext, visitor: Visitor): Any {
        val value = visitor.visit(ctx.value)
        return when {
            (ctx.values.literalList != null) -> {
                ctx.values.string_literal()
                    .map{ it.text.replace("'", "", true) }
                    .map{ it.replace("'", "") }
                    .contains(value)
            }
            ctx.values.storedList != null -> {

                val list = visitor.lists[ctx.values.string_literal()[0].text.replace("\'", "")]

                list?.contains(value.toString()) ?: false
            }
            ctx.values.validProperty() != null ->
                (ValidPropertyContextEvaluator().evaluate(ctx.values.validProperty(), visitor) as List<*>)
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
               visitor.lists[ctx.values.string_literal()[0].text.replace("\'", "")]
            }
            ctx.values.validProperty() != null -> (ValidPropertyContextEvaluator().evaluate(ctx.values.validProperty(), visitor) as List<*>)

            else -> error("Unexpected symbol ${ctx.values}")
        }
        return list?.map { elem -> value.startsWith(elem.toString(), true) }
            ?.reduce { fst, snd -> fst || snd } ?: false
    }


}