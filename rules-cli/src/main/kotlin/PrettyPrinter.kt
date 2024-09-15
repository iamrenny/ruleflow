import com.github.iamrenny.ruleflow.RuleFlowLanguageBaseListener
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser
import org.antlr.v4.runtime.TokenStream

open class PrettyPrinter(val tokens: TokenStream) : RuleFlowLanguageBaseListener() {
    var indentLevel: Int = 0

    override fun enterWorkflow(ctx: RuleFlowLanguageParser.WorkflowContext) {
        print("workflow ")
        indentLevel++
    }

    override fun exitWorkflow(ctx: RuleFlowLanguageParser.WorkflowContext) {
        indentLevel--
    }

    override fun enterWorkflow_name(ctx: RuleFlowLanguageParser.Workflow_nameContext) {
        println(ctx.text)
    }

    override fun enterRulesets(ctx: RuleFlowLanguageParser.RulesetsContext) {
        printIndent()
        println("ruleset ${ctx.name().text}")
        indentLevel++
    }

    override fun exitRulesets(ctx: RuleFlowLanguageParser.RulesetsContext) {
        indentLevel--
    }

    override fun enterRules(ctx: RuleFlowLanguageParser.RulesContext) {
        printIndent()
        val exprTokens = tokens.getText(ctx.start, ctx.stop)
        println(exprTokens)
        indentLevel++
    }

    override fun exitRules(ctx: RuleFlowLanguageParser.RulesContext) {
        indentLevel--
    }


    override fun enterDefault_result(ctx: RuleFlowLanguageParser.Default_resultContext) {
        printIndent()
        println("default ${ctx.text}")
    }

    override fun exitDefault_result(ctx: RuleFlowLanguageParser.Default_resultContext?) {
        indentLevel--
        printIndent()
        println("end")
    }
    open fun printIndent() {
        repeat(indentLevel) { print("  ") }
    }
}