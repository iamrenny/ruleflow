import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANABaseVisitor
import com.rappi.fraud.analang.ANAParser

val data = mapOf<String, Any>(
    "event" to "create-order",
    "order_id" to "818811",
    "user_id" to "409012",
    "store_type" to "restaurant",
    "payment_type" to "credit_card",
    "payment_methods" to listOf(
        mapOf(
            "payment_method_type" to "credit_card",
            "vendor" to "visa",
            "country" to "US"
        )
    )
)

class EvaluateRulesetVisitor: ANABaseVisitor<String>() {
    override fun visitParse(ctx: ANAParser.ParseContext): String {
        return visitWorkflow(ctx.workflow())
    }
    override fun visitWorkflow(ctx: ANAParser.WorkflowContext): String {
        ctx.stmt_list().stmt().forEach { it ->
            if(EvaluateConditionVisitor().visitCond(it.cond())) return it.result_value().text
        }

        return ctx.stmt_list().default_stmt().result_value().text
    }

    private inner class EvaluateConditionVisitor: ANABaseVisitor<Boolean>() {
        override fun visitCond(ctx: ANAParser.CondContext): Boolean {
            if (ctx.ID() != null) {
                if(data[ctx.ID().text] == null ) return false // no risk
                when {
                    ctx.EQ() != null  ->
                        return data[ctx.ID().text] == ctx.literal_value().STRING_LITERAL().text.replace("'", "")
                    ctx.NOT_EQ1() != null  ->
                        return data[ctx.ID().text] != ctx.literal_value().STRING_LITERAL().text.replace("'", "")
                }
            }
            if(ctx.cond() != null) {
                when {
                    ctx.K_AND() != null -> return visitCond(ctx.cond(0)) && visitCond(ctx.cond(1))
                    ctx.K_OR() != null ->  return visitCond(ctx.cond(0)) || visitCond(ctx.cond(1))
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