import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class RuleEngineTest {

    companion object {
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

        @JvmStatic
        @Suppress("unused")
        fun testResult(): Array<Arguments> {
            return arrayOf(
                Arguments.of(
                    "samples/test_result_default.ANA",
                    WorkflowResult(workflow = "create-order", risk = "allow")
                ),
                Arguments.of(
                    "samples/test_result_allow.ANA",
                    WorkflowResult("create-order", "ok", "good_user", "allow")
                ),
                Arguments.of(
                    "samples/test_result_block.ANA",
                    WorkflowResult("create-order", "bad", "bad_user", "block")
                ),
                Arguments.of(
                    "samples/test_result_prevent.ANA",
                    WorkflowResult("create-order", "prevent", "prevent_user", "prevent")
                )
            )
        }
    }

    @Test
    fun testDeepId() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    rappi_user user.type.name.pepe.another = 'RAPPI_USER' return block
                default allow
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "user" to mapOf(
                        "type" to mapOf(
                            "name" to mapOf(
                                "pepe" to mapOf("another" to "RAPPI_USER")
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
                    item_a order.items.any { type = 'a' } return block
                default allow
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "order" to mapOf(
                        "items" to listOf(
                            mapOf("type" to "c"),
                            mapOf("type" to "b"),
                            mapOf("type" to "a")
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
                    item_a order.items.any { type = 'a' } return block
                default allow
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", risk = "allow"), ruleEngine.evaluate(
                mapOf(
                    "order" to mapOf(
                        "items" to listOf(
                            mapOf("type" to "c"),
                            mapOf("type" to "b"),
                            mapOf("type" to "e")
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
                    item_a items.all { type = 'a' } return block
                default allow
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "items" to listOf(
                        mapOf("type" to "a"),
                        mapOf("type" to "a"),
                        mapOf("type" to "a")
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
                    item_a items.all { type = 'a' } return allow
                default allow
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", risk = "allow"), ruleEngine.evaluate(
                mapOf(
                    "items" to listOf(
                        mapOf("type" to "b"),
                        mapOf("type" to "a"),
                        mapOf("type" to "a")
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
                    rappi_user_has_more_than_1 items.count { type = 'a' } >= 1 return block
                default allow
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user_has_more_than_1", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "items" to listOf(
                        mapOf("type" to "b"),
                        mapOf("type" to "a"),
                        mapOf("type" to "a")
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
                    rappi_user_avg_gte_0_5 items.average { type = 'a' } > 0.5 return block
                default allow
            end
        """.trimIndent()

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user_avg_gte_0_5", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "items" to listOf(
                        mapOf("type" to "b"),
                        mapOf("type" to "a"),
                        mapOf("type" to "a")
                    )
                )
            )
        )
    }

    @Test
    fun testLogical() {
        val file = javaClass.classLoader.getResourceAsStream("samples/test_logical.ANA")!!.reader().readText()
        val ruleEngine = RuleEngine(file)
        Assertions.assertEquals("block", ruleEngine.evaluate(data).risk)
    }

    @ParameterizedTest
    @MethodSource("testResult")
    fun testResult(workflowPath: String, expected: WorkflowResult) {
        val workflow = javaClass.classLoader.getResourceAsStream(workflowPath)!!.reader().readText()
        val actual = RuleEngine(workflow).evaluate(data)
        Assertions.assertEquals(expected, actual)
    }
}