import com.rappi.analang.listeners.ErrorListener
import com.rappi.analang.visitors.RulesetVisitor
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser("ANA Lang CLI")
    val input by parser.option(ArgType.String, shortName = "f", fullName = "input-file", description = "Input file")
    val workflowFilename by parser.option(ArgType.String, shortName = "w", fullName = "workflow-file", description = "Workflow file").required()
    val output by parser.option(ArgType.String, shortName = "o", fullName = "output", description = "Output file name")
    val format by parser.option(ArgType.Choice<Format>(), shortName = "F",
        description = "Format for output file").default(Format.JSON)
    val prettyPrint by parser.option(ArgType.Boolean, shortName = "p", fullName = "pretty-print",
        description = "Workflow Indentation and Format").default(false)
    val debug by parser.option(ArgType.Boolean, shortName = "d", fullName = "debug", description = "Turn on debug mode").default(false)

    parser.parse(args)

    val workflowFile = File(workflowFilename).readText()
    val result = when(prettyPrint) {
        true -> {
            formatter(workflowFile)
        }
        else ->  {
            val inputData =  File(input).readText()
            evaluate(inputData, workflowFile)
        }
    }

    print(result)
}

enum class Format {
    HTML,
    JSON
}
fun evaluate(inputData: String, workflow: String): String {
    val input = CharStreams.fromString(workflow)
    val lexer = ANALexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = ANAParser(tokens)
    parser.addErrorListener(ErrorListener())
    val tree = parser.parse()

    val inputMap = Json.decodeFromString<JsonObject>(inputData)
    val result = RulesetVisitor(inputMap, mapOf())
        .visit(tree)

    return """
        Execution Result for Workflow: ${result.workflow}:
            Matched Ruleset: ${result.ruleSet}
            Matched Rule: ${result.rule}
            Risk: ${result.result}
            Actions With Params: ${result.actionsWithParams}
            Warnings: ${result.warnings.ifEmpty { "None" }}
            Is Error?: ${result.error}
    """.trimIndent()
}

fun formatter(workflow: String) {
    val input = CharStreams.fromString(workflow)
    val lexer = ANALexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = ANAParser(tokens)
    parser.addErrorListener(ErrorListener())
    val tree = parser.parse()

    val printer = PrettyPrinter(tokens)
    val walker = ParseTreeWalker()
    walker.walk(printer, tree)
}

