import com.rappi.analang.listeners.ErrorListener
import com.rappi.analang.visitors.RulesetVisitor
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File

var inputFile: String? = null


fun main(args: Array<String>) {
    val parser = ArgParser("ANA Lang CLI")
    val input by parser.option(ArgType.String, shortName = "F", fullName = "input-file", description = "Input file")
    val workflowFilename by parser.option(ArgType.String, shortName = "w", fullName = "workflow-file", description = "Workflow file")
    val output by parser.option(ArgType.String, shortName = "o", fullName = "output", description = "Output file name")

    val prettyPrint by parser.option(ArgType.Boolean, shortName = "p", fullName = "pretty-print",
        description = "Workflow Indentation and Format").default(false)
    val debug by parser.option(ArgType.Boolean, shortName = "d", fullName = "debug", description = "Turn on debug mode").default(false)
    val isInteractive by parser.option(ArgType.Boolean, shortName = "i", fullName = "interactive", description = "Enable interactive terminal")
        .default(false)
    val filter by parser.option(ArgType.String, shortName = "f", fullName = "filter")

    parser.parse(args)
    val workflowFile = if(!workflowFilename.isNullOrEmpty())
        File(workflowFilename).readText() else null

    inputFile = if(!inputFile.isNullOrEmpty())
        File(input).readText() else null

    when {
        isInteractive -> {
            interactive(inputFile)
        }
        prettyPrint -> {
            if(workflowFile == null)
                throw RuntimeException("Workflow file is required for pretty print")
            formatter(workflowFile)
        }
        filter != null -> {
            if (workflowFile == null)
                throw RuntimeException("Workflow file is required for pretty print")
            filter(workflowFile, filter!!)
        }
        else ->  {
            if(workflowFile == null)
                throw RuntimeException("Workflow file is required for output")
            if(inputFile == null)
                throw RuntimeException("Input file is required for output")

            evaluate(inputFile!!, workflowFile)
        }
    }
}
/**
 *  interactive function is used to run the interactive terminal
 **/
fun interactive(file: String?) {
    println("Enter input data or 'q' to quit:")
    while (true) {
        inputFile = if(file != null) {
            getFile()
        } else { inputFile }

        print("#>")

        val input = readln()
        if (input == "o" || input == "open") {
            inputFile = getFile()
            println(Json.encodeToJsonElement(inputFile))
        } else if (input == "e" || input == "evaluate") {
            val inputData =  getFile()
            print("Enter workflow file name")
            val workflowName = readln()
            val workflowData = File(workflowName).readText()
            evaluate(inputData!!, workflowData)
        } else if (input == "r" || input == "run") {
            try {
                if(inputFile == null)
                    inputFile = getFile()
                println("Enter rule for evaluation. Type \\h for help with commands or press Crtl + C to exit")
                while (true) {
                    try {
                        print("run>")
                        val rule = readln()
                        if(rule == "\\j") {
                            println(inputFile)
                        } else if(rule == "\\o") {
                            inputFile = getFile()
                        } else if(rule == "\\h") {
                            println("""
                                \h show this message
                                \j shows the loaded json
                                \o opens a new input file
                            """.trimIndent())
                        } else if(rule == "\\q") {
                            return
                        } else {
                            println(run(inputFile!!, rule))
                        }
                    } catch (e: Exception){
                        println(e.message)
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }

        }
        else if (input == "q" || input == "quit") {
            break
        }
    }
}

private fun getFile(): String? {
    while(true) {
        try {
            print("Enter input file name in JSON format: ")
            val inputFileName = readln()
            val inputData = File(inputFileName).readText()
            return inputData
        } catch (e: Exception) {
            println(e.message)
        }
    }

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

    val inputMap = Json.decodeFromString< JsonObject>(inputData)
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

fun run(inputData: String, rule: String): String {

    val jsonObject = Json.decodeFromString<JsonObject>(inputData)
    val map = serialize(jsonObject)

    return RuleEvaluator(rule, map, mapOf())
        .evaluate()
}

fun serialize(jsonObject: JsonElement): Map<String, *> {
    return jsonObject.jsonObject.mapValues { (_, jsonElement) ->
        when (jsonElement) {
            is JsonPrimitive -> jsonElement.content
            is JsonObject -> serialize(jsonElement)
            is JsonArray -> jsonElement.toList()
                .map {
                    serialize(it)
                }
            else -> throw IllegalArgumentException("Unsupported JSON element type: ${jsonElement::class}")
        }
    }
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

fun filter(workflow: String, criteria: String) {
    val input = CharStreams.fromString(workflow)
    val lexer = ANALexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = ANAParser(tokens)
    parser.addErrorListener(ErrorListener())
    val tree = parser.parse()

    val printer = FilterListener(tokens, criteria)
    val walker = ParseTreeWalker()
    walker.walk(printer, tree)
}
fun json(input: String){
    println(input)
}

