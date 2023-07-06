import com.rappi.analang.visitors.Visitor
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.slf4j.LoggerFactory

class RuleEvaluator(val inputRule: String, private val data: Map<String, *>, private val lists:  Map<String, Set<String>>) {
    val syntaxErrorListener = SyntaxErrorListener()

    fun evaluate(): String {
        val input = CharStreams.fromString(inputRule)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        parser.removeErrorListeners()  // Remove the default error listeners
        parser.addErrorListener(syntaxErrorListener)
        val ctx = parser.expr()

        if(syntaxErrorListener.failed)
            return "error: ${syntaxErrorListener.errorMessage!!}" +
                    "\n${tokens.getText(ctx.start, ctx.stop)}"


        val ruleEvaluator = Visitor(data, lists, data)

        return try {
            return ruleEvaluator.visit(ctx).toString()
        }  catch (ex: Exception) {
            println(
                "Error while evaluating rule ${tokens.getText(ctx.start, ctx.stop)}: ${ex.message}"
            )
            "error: ${ex.message!!}\n${tokens.getText(ctx.start, ctx.stop)}"
        }
    }
}
class SyntaxErrorListener : BaseErrorListener() {
    var failed: Boolean = false
    var errorMessage: String? = null
    override fun syntaxError(recognizer: Recognizer<*, *>?,
                             offendingSymbol: Any?,
                             line: Int,
                             charPositionInLine: Int,
                             msg: String?,
                             e: RecognitionException?) {
        errorMessage = "Error at line $line:$charPositionInLine - $msg"
        failed = true
    }
}
