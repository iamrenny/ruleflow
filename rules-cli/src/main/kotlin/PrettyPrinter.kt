import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANAParser
import org.antlr.v4.runtime.TokenStream

class PrettyPrinter(val tokens: TokenStream) : ANABaseListener() {
    private var indentLevel: Int = 0

    override fun enterWorkflow(ctx: ANAParser.WorkflowContext) {
        print("workflow ")
        indentLevel++
    }

    override fun exitWorkflow(ctx: ANAParser.WorkflowContext) {
        indentLevel--
    }

    override fun enterWorkflow_name(ctx: ANAParser.Workflow_nameContext) {
        println(ctx.text)
    }

    override fun enterRulesets(ctx: ANAParser.RulesetsContext) {
        printIndent()
        println("ruleset ${ctx.name().text}")
        indentLevel++
    }

    override fun exitRulesets(ctx: ANAParser.RulesetsContext) {
        indentLevel--
    }

    override fun enterRules(ctx: ANAParser.RulesContext) {
        printIndent()
        val exprTokens = tokens.getText(ctx.start, ctx.stop)
        println(exprTokens)
        indentLevel++
    }

    override fun exitRules(ctx: ANAParser.RulesContext) {
        indentLevel--
    }


    override fun enterDefault_result(ctx: ANAParser.Default_resultContext) {
        printIndent()
        println("default ${ctx.text}")
    }

    override fun exitDefault_result(ctx: ANAParser.Default_resultContext?) {
        indentLevel--
        printIndent()
        println("end")
    }
    private fun printIndent() {
        repeat(indentLevel) { print("  ") }
    }
}