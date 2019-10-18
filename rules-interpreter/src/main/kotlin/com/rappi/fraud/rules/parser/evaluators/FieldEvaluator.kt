package com.rappi.fraud.rules.parser.evaluators

import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser

class FieldEvaluator(private val data: Map<String, *>) : ANABaseVisitor<Any>() {

    override fun visitField(ctx: ANAParser.FieldContext?): Any? {
        return when (ctx) {
            null -> null
            else -> visitFieldRecursive(ctx)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun visitFieldRecursive(ctx: ANAParser.FieldContext): Any? {
        val queue = mutableListOf(ctx)
        ctx.field().forEach {
            queue.add(it)
        }
        var r = (data as MutableMap)
        queue.take(ctx.field().size).forEach {
            if (r[it.ID().text] is Map<*, *>) {
                r = r[it.ID().text] as MutableMap<String, *>
            } else {
                throw RuntimeException("${it.parent.text} is an invalid operation")
            }
        }
        return r[queue.last().ID().text]
    }

}