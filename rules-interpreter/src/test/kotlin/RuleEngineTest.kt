import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

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

        @JvmStatic
        @Suppress("UNUSED")
        fun testNullable(): Array<Arguments> {
            return arrayOf(
                Arguments.of(
                    """
                        workflow 'test'
                            ruleset 'dummy'
                                'has_user' user <> NULL return allow
                            default prevent
                        end
                    """,
                    WorkflowResult("test", "dummy", "has_user", "allow")
                ),
                Arguments.of(
                    """
                        workflow 'test'
                            ruleset 'dummy'
                                'has_user' user.type = NULL return block
                            default allow
                        end
                    """,
                    WorkflowResult("test", "dummy", "has_user", "block")
                )
            )
        }
    }

    @Test
    fun testNestedProperty() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user' user.type.name.pepe.another = 'RAPPI_USER' return block
                default allow
            end
        """

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
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """

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
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """

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
    fun testListAnyNotCollection() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """

        Assertions.assertThrows(RuntimeException::class.java) {
            RuleEngine(workflow).evaluate(mapOf("items" to "1"))
        }
    }

    @Test
    fun testListAll() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' items.all { type = 'a' } return block
                default allow
            end
        """

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
                    'item_a' items.all { type = 'a' } return allow
                default allow
            end
        """

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
    fun testListAllNotCollection() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.all { type = 'a' } return block
                default allow
            end
        """

        Assertions.assertThrows(RuntimeException::class.java) {
            RuleEngine(workflow).evaluate(mapOf("items" to "1"))
        }
    }

    @Test
    fun testCount() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_has_more_than_1' items.count { type = 'a' } >= 1 return block
                default allow
            end
        """

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
    fun testCountWithoutPredicate() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_has_3' items.count() = 3 return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                WorkflowResult("test", "dummy", "rappi_user_has_3", "block"),
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
    fun testCountNotCollection() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_has_more_than_1' order.items.count { type = 'a' } return block
                default allow
            end
        """

        Assertions.assertThrows(RuntimeException::class.java) {
            RuleEngine(workflow).evaluate(mapOf("items" to "1"))
        }
    }

    @Test
    fun testAverage() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_avg_gte_0_5' items.average { type = 'a' } > 0.5 return block
                default allow
            end
        """

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
    fun testAverageNotCollection() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_avg_gte_0_5' order.items.average { type = 'a' } > 0.5 return block
                default allow
            end
        """

        Assertions.assertThrows(RuntimeException::class.java) {
            RuleEngine(workflow).evaluate(mapOf("items" to "1"))
        }
    }

    @Test
    fun testDistinctWithPredicate() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_has_2' items.distinct { type = 'a' }.count() = 2 return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                WorkflowResult("test", "dummy", "rappi_user_has_2", "block"),
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
    fun testDistinctWithSelector() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_has_3' items.distinct { type }.count() = 3 return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
                WorkflowResult("test", "dummy", "rappi_user_has_3", "block"),
                ruleEngine.evaluate(
                        mapOf(
                                "items" to listOf(
                                        mapOf("type" to "b"),
                                        mapOf("type" to "a"),
                                        mapOf("type" to "c")
                                )
                        )
                )
        )
    }

    @Test
    fun testDistinctNotCollection() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_has_more_than_1' order.items.distinct() return block
                default allow
            end
        """

        Assertions.assertThrows(RuntimeException::class.java) {
            RuleEngine(workflow).evaluate(mapOf("items" to "1"))
        }
    }

    @Test
    fun testMath() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi' (((((subtotal + discount) / discount) * subtotal) - subtotal) + 30) = 80 return allow
                default prevent
            end
        """

        val actualRisk = RuleEngine(workflow).evaluate(mapOf("subtotal" to 50, "discount" to 50)).risk
        Assertions.assertEquals("allow", actualRisk)
    }

    @Test
    fun testLogical() {
        val file = javaClass.classLoader.getResourceAsStream("samples/test_logical.ANA")!!.reader().readText()
        val ruleEngine = RuleEngine(file)
        val expected = WorkflowResult("test", "test", "payment_type", "block", setOf("manual_review"))
        Assertions.assertEquals(expected, ruleEngine.evaluate(data))
    }

    @ParameterizedTest
    @MethodSource("testResult")
    fun testResult(workflowPath: String, expected: WorkflowResult) {
        val workflow = javaClass.classLoader.getResourceAsStream(workflowPath)!!.reader().readText()
        val actual = RuleEngine(workflow).evaluate(data)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun testValidateAndGetWorkflowName() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_avg_gte_0_5' items.average { type = 'a' } > 0.5 return block
                default allow
            end
        """
        val actualName = RuleEngine(workflow).validateAndGetWorkflowName()
        Assertions.assertEquals("test", actualName)
    }

    @Test
    fun testValidateAndGetWorkflowNameWhenInvalid() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            RuleEngine("workflow").validateAndGetWorkflowName()
        }
    }

    @ParameterizedTest
    @MethodSource("testNullable")
    fun testNullable(workflow: String, expectedRisk: WorkflowResult) {
        val actualRisk = RuleEngine(workflow).evaluate(data)
        Assertions.assertEquals(expectedRisk, actualRisk)
    }

    @Test
    fun testDateDiff() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' paymentMethods.count { dateDiff(hour, currentDate(), createdAt) = 1 } > 0 return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "registration_attempts", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "paymentMethods" to listOf(
                        mapOf("createdAt" to LocalDateTime.now().minusSeconds(60 * 60).toString()),
                        mapOf("createdAt" to LocalDateTime.now().toString()),
                        mapOf("createdAt" to LocalDateTime.now().toString())
                    )
                )
            )
        )
    }

    @Test
    fun testDateDiffDaysBlock() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' paymentMethods.count { dateDiff(day, currentDate(), createdAt) = 0 } > 0 return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "registration_attempts", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "paymentMethods" to listOf(
                        mapOf("createdAt" to LocalDateTime.now())
                    )
                )
            )
        )
    }

    @Test
    fun testDateDiffDaysAllow() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' paymentMethods.count { dateDiff(day, currentDate(), createdAt) = 0 } > 0 return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", risk = "allow"),
            ruleEngine.evaluate(
                mapOf(
                    "paymentMethods" to listOf(
                        mapOf("createdAt" to LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(1).plusHours(23)
                            .plusMinutes(59).plusSeconds(59))
                    )
                )
            )
        )
    }
}