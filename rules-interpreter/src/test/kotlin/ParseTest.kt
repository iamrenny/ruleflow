import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.EvaluateRulesetVisitor
import com.rappi.fraud.rules.parser.RuleEngine
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParseTest {
    val data = mapOf(
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
        ),
        "test" to 3,
        "order" to mapOf(
            "items" to listOf(
                mapOf(
                    "type" to "restaurant",
                    "amount" to 10
                ),
                mapOf(
                    "type" to "whim",
                    "amount" to 10
                )
            )
        )
    )

    val target = EvaluateRulesetVisitor(data)

    @Test
    fun testParse(){
        val file =  javaClass.classLoader.getResourceAsStream("samples/test.ANA").reader().readText()
        val ruleEngine = RuleEngine(file)
        Assertions.assertEquals("looks_ok", ruleEngine.evaluate(data))
    }

    @Test
    fun parseDeepId() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    user.type.name.pepe.another = 'RAPPI_USER' return 'looks_ok'
                    default return 'needs_validation'
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals("looks_ok", ruleEngine.evaluate(mapOf(
            "user" to mapOf(
                "type" to mapOf(
                    "name" to mapOf(
                        "pepe" to mapOf(
                            "another" to "RAPPI_USER")))
        ))))
    }
    @Test
    fun testParse2(){
        val file =  javaClass.classLoader.getResourceAsStream("samples/test2.ANA")
        val input = CharStreams.fromStream(file)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        val tree = parser.parse()
        print(target.visit(tree))
        Assertions.assertEquals("needs_validation", target.visit(tree))
    }


    @Test
    fun testParse3(){
        val file =  javaClass.classLoader.getResourceAsStream("samples/test3.ANA")
        val input = CharStreams.fromStream(file)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        val tree = parser.parse()
        print(target.visit(tree))
    }
}