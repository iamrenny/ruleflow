import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANAParser
import org.antlr.v4.runtime.TokenStream

class FilterListener(tokens: TokenStream, val criteria: String): PrettyPrinter(tokens) {
    override fun enterRules(ctx: ANAParser.RulesContext) {
        if(tokens.getText(ctx.start, ctx.stop).contains(criteria)) {
            super.enterRules(ctx)
        }
    }

    override fun exitRules(ctx: ANAParser.RulesContext) {
        if (tokens.getText(ctx.start, ctx.stop).contains(criteria)) {
            super.exitRules(ctx)
        }
    }
}