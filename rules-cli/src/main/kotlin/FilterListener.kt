import com.github.iamrenny.ruleflow.RuleFlowLanguageBaseListener
import com.github.iamrenny.ruleflow.RuleFlowLanguageParser
import org.antlr.v4.runtime.TokenStream

class FilterListener(tokens: TokenStream, val criteria: String): PrettyPrinter(tokens) {
    override fun enterRules(ctx: RuleFlowLanguageParser.RulesContext) {
        if(tokens.getText(ctx.start, ctx.stop).contains(criteria)) {
            super.enterRules(ctx)
        }
    }

    override fun exitRules(ctx: RuleFlowLanguageParser.RulesContext) {
        if (tokens.getText(ctx.start, ctx.stop).contains(criteria)) {
            super.exitRules(ctx)
        }
    }
}