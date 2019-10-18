import com.rappi.fraud.rules.parser.RuleEngine
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RuleEngineTest {

    private val data = mapOf(
            "event" to "create-order",
            "order_id" to "818811",
            "user_id" to "409012",
            "user" to mapOf(
                    "test" to mapOf(
                            "pepe" to "3"
                    )
            ),
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

    @Test
    fun testParse() {
        val file = javaClass.classLoader.getResourceAsStream("samples/test.ANA")!!.reader().readText()
        val ruleEngine = RuleEngine(file)
        Assertions.assertEquals("looks_ok", ruleEngine.evaluate(data))
    }

    @Test
    fun testDeepId() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    rappi_user user.type.name.pepe.another = 'RAPPI_USER' return 'looks_ok'
                    default return 'needs_validation'
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                "looks_ok", ruleEngine.evaluate(
                mapOf(
                        "user" to mapOf(
                                "type" to mapOf(
                                        "name" to mapOf(
                                                "pepe" to mapOf(
                                                        "another" to "RAPPI_USER"
                                                )
                                        )
                                )
                        )
                )
        )
        )
    }

    @Test
    fun testListAny() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    item_a order.items.any { type = 'a' } return 'looks_ok'
                    default return 'needs_validation'
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                "looks_ok", ruleEngine.evaluate(
                mapOf(
                        "order" to mapOf(
                                "items" to listOf(
                                        mapOf(
                                                "type" to "c"
                                        ),
                                        mapOf(
                                                "type" to "b"
                                        ),
                                        mapOf(
                                                "type" to "a"
                                        )
                                )
                        )
                )
        )
        )
    }

    @Test
    fun testListAnyMissing() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    item_a order.items.any { type = 'a' } return 'looks_ok'
                    default return 'needs_validation'
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                "needs_validation", ruleEngine.evaluate(
                mapOf(
                        "order" to mapOf(
                                "items" to listOf(
                                        mapOf(
                                                "type" to "c"
                                        ),
                                        mapOf(
                                                "type" to "b"
                                        ),
                                        mapOf(
                                                "type" to "e"
                                        )
                                )
                        )
                )
        )
        )
    }

    @Test
    fun testListAll() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    item_a items.all { type = 'a' } return 'looks_ok'
                    default return 'needs_validation'
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                "looks_ok", ruleEngine.evaluate(
                mapOf(
                        "items" to listOf(
                                mapOf(
                                        "type" to "a"
                                ),
                                mapOf(
                                        "type" to "a"
                                ),
                                mapOf(
                                        "type" to "a"
                                )
                        )
                )
        )
        )
    }

    @Test
    fun testListAllMissing() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    item_a items.all { type = 'a' } return 'looks_ok'
                    default return 'needs_validation'
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                "needs_validation", ruleEngine.evaluate(
                mapOf(
                        "items" to listOf(
                                mapOf(
                                        "type" to "b"
                                ),
                                mapOf(
                                        "type" to "a"
                                ),
                                mapOf(
                                        "type" to "a"
                                )
                        )
                )
        )
        )
    }

    @Test
    fun testCount() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    rappi_user_has_more_than_2_a items.count { type = 'a' } >= 2 return 'looks_ok'
                    default return 'needs_validation'
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                "looks_ok", ruleEngine.evaluate(
                mapOf(
                        "items" to listOf(
                                mapOf(
                                        "type" to "b"
                                ),
                                mapOf(
                                        "type" to "a"
                                ),
                                mapOf(
                                        "type" to "a"
                                )
                        )
                )
        )
        )
    }

    @Test
    fun testAverage() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    rappi_user_has_more_than_2_a items.average { type = 'a' } >= 0.5 return 'looks_ok'
                    default return 'needs_validation'
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                "looks_ok", ruleEngine.evaluate(
                mapOf(
                        "items" to listOf(
                                mapOf(
                                        "type" to "b"
                                ),
                                mapOf(
                                        "type" to "a"
                                ),
                                mapOf(
                                        "type" to "a"
                                )
                        )
                )
        )
        )
    }

    @Test
    fun testLogical() {
        val file = javaClass.classLoader.getResourceAsStream("samples/test3.ANA")!!.reader().readText()
        val ruleEngine = RuleEngine(file)
        Assertions.assertEquals("looks_ok", ruleEngine.evaluate(data))
    }
}