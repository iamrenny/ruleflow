import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.listeners.ErrorListener
import com.rappi.fraud.rules.parser.visitors.RulesetVisitor
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.Tree
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
            Risk: ${result.risk}
            Actions With Params: ${result.actionsWithParams}
            Warnings: ${result.warnings.ifEmpty { "None" }}
            Is Error?: ${result.error}
    """.trimIndent()
}

fun formatter(workflow: String): String {
    val input = CharStreams.fromString(workflow)
    val lexer = ANALexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = ANAParser(tokens)
    parser.addErrorListener(ErrorListener())
    val tree = parser.parse()
    return TreeUtils.toString(tree)
}

object TreeUtils {
    /** Platform dependent end-of-line marker  */
    val Eol = System.lineSeparator()

    /** The literal indent char(s) used for pretty-printing  */
    const val Indents = " "
    var level = 0


    /**
     * Pretty print out a whole tree. [.getNodeText] is used on the node payloads to get the text
     * for the nodes. (Derived from Trees.toStringTree(....))
     */
    fun toPrettyTree(t: Tree): String {
        return process(t.getChild(0))
        .replace("(?m)^\\s+$".toRegex(), "").replace("\\r?\\n\\r?\\n".toRegex(), Eol)
    }

    fun toString(t: Tree): String {
        val sb = StringBuilder()
        if(t.childCount == 0)
            return t.toString()

        for(i in 0 until t.childCount) {
            sb.append(" ")
            sb.append(toString(t.getChild(i)))
        }

        return sb.toString()
    }


    private fun process(t: Tree): String {
        if (t.childCount == 0)
            return "$t"

        val sb = StringBuilder()

        level++
        for(i in 0 until t.childCount) {
            sb.append(process(t.getChild(i)))
        }
        level--
        sb.append(lead(level))
        return sb.toString()
    }

    private fun lead(level: Int): String? {
        val sb = StringBuilder()
        if (level > 0) {
            sb.append(Eol)
            for (cnt in 0 until level) {
                sb.append(Indents)
            }
        }
        return sb.toString()
    }
}
