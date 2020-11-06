import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.vo.WorkflowInfo
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class WorkflowEvaluatorTest {

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
                    WorkflowResult(workflow = "create-order", ruleSet = "default", rule = "default",  risk = "allow", workflowInfo = WorkflowInfo("", ""))
                ),
                Arguments.of(
                    "samples/test_result_allow.ANA",
                    WorkflowResult("create-order", "ok", "good_user", "allow", workflowInfo = WorkflowInfo("", ""))
                ),
                Arguments.of(
                    "samples/test_result_block.ANA",
                    WorkflowResult("create-order", "bad", "bad_user", "block", workflowInfo = WorkflowInfo("", ""))
                ),
                Arguments.of(
                    "samples/test_result_prevent.ANA",
                    WorkflowResult("create-order", "prevent", "prevent_user", "prevent", workflowInfo = WorkflowInfo("", ""))
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
                    WorkflowResult("test", "dummy", "has_user", "allow", workflowInfo = WorkflowInfo("", ""))
                ),
                Arguments.of(
                    """
                        workflow 'test'
                            ruleset 'dummy'
                                'has_user' user.type <> null and user.type = 'test' return block
                                'not_has_user' order_id = '818811' return block
                            default allow
                        end
                    """,
                    WorkflowResult("test", "dummy", "not_has_user", "block", setOf(), setOf("type field cannot be found"), workflowInfo = WorkflowInfo("", ""))
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user", "block", workflowInfo = WorkflowInfo("", "")),
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
    fun testCount() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_has_more_than_1' items.count { type = 'a' } >= 1 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user_has_more_than_1", "block", workflowInfo = WorkflowInfo("", "")),
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user_has_3", "block", workflowInfo = WorkflowInfo("", "")),
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

        val result = WorkflowEvaluator(workflow).evaluate(mapOf("items" to 1))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", warnings = setOf("order field cannot be found"), workflowInfo = WorkflowInfo("", "")), result)
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user_avg_gte_0_5", "block", workflowInfo = WorkflowInfo("", "")),
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

        val result = WorkflowEvaluator(workflow).evaluate(mapOf("items" to listOf("a", "c")))
        Assertions.assertEquals(WorkflowResult(workflow = "test",  ruleSet = "default", rule = "default", risk = "allow", warnings = setOf("order field cannot be found"), workflowInfo = WorkflowInfo("", "")), result)
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user_has_2", "block", workflowInfo = WorkflowInfo("", "")),
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "rappi_user_has_3", "block", workflowInfo = WorkflowInfo("", "")),
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

        val result = WorkflowEvaluator(workflow).evaluate(mapOf("items" to 1))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", warnings = setOf("order field cannot be found"), workflowInfo = WorkflowInfo("", "")), result)
    }

    @Test
    fun testNestedNotFound() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_has_more_than_1' order.items.distinct() return block
                default allow
            end
        """

        val result = WorkflowEvaluator(workflow).evaluate(mapOf("order" to mapOf("items2" to "13")))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", warnings = setOf("items field cannot be found"), workflowInfo = WorkflowInfo("", "")), result)
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

        val actualRisk = WorkflowEvaluator(workflow).evaluate(mapOf("subtotal" to 50, "discount" to 50)).risk
        Assertions.assertEquals("allow", actualRisk)
    }

    @Test
    fun testMathNegative() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi' subtotal * -1 = -50 return allow
                default prevent
            end
        """

        val actualRisk = WorkflowEvaluator(workflow).evaluate(mapOf("subtotal" to 50)).risk
        Assertions.assertEquals("allow", actualRisk)
    }

    @Test
    fun testLogical() {
        val file = javaClass.classLoader.getResourceAsStream("samples/test_logical.ANA")!!.reader().readText()
        val ruleEngine = WorkflowEvaluator(file)
        val expected = WorkflowResult("test", "test", "payment_type", "block", setOf("manual_review"), setOf(), mapOf("manual_review" to mapOf()), workflowInfo = WorkflowInfo("", ""))
        Assertions.assertEquals(expected, ruleEngine.evaluate(data))
    }

    @ParameterizedTest
    @MethodSource("testResult")
    fun testResult(workflowPath: String, expected: WorkflowResult) {
        val workflow = javaClass.classLoader.getResourceAsStream(workflowPath)!!.reader().readText()
        val actual = WorkflowEvaluator(workflow).evaluate(data)
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
        val actualName = WorkflowEvaluator(workflow).validateAndGetWorkflowName()
        Assertions.assertEquals("test", actualName)
    }

    @Test
    fun testValidateAndGetWorkflowNameWhenInvalid() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            WorkflowEvaluator("workflow").validateAndGetWorkflowName()
        }
    }

    @ParameterizedTest
    @MethodSource("testNullable")
    fun testNullable(workflow: String, expectedRisk: WorkflowResult) {
        val actualRisk = WorkflowEvaluator(workflow).evaluate(data)
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "registration_attempts", "block", workflowInfo = WorkflowInfo("", "")),
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
    fun testDateDiffInMinutes() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' paymentMethods.count { dateDiff(minute, currentDate(), createdAt) = 60 } > 0 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "registration_attempts", "block", workflowInfo = WorkflowInfo("", "")),
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
    fun testDateDiffInMinutesIsZero() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' paymentMethods.count { dateDiff(minute, currentDate(), createdAt) = 0 } > 0 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "registration_attempts", "block", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "paymentMethods" to listOf(
                        mapOf("createdAt" to LocalDateTime.now().toString()),
                        mapOf("createdAt" to LocalDateTime.now().toString()),
                        mapOf("createdAt" to LocalDateTime.now().toString())
                    )
                )
            )
        )
    }

    @Test
    fun `given a negative result in datediff operation must return absolute value`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' paymentMethods.count { dateDiff(minute, createdAt, currentDate()) = 60 } > 0 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "registration_attempts", "block", workflowInfo = WorkflowInfo("", "")),
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "registration_attempts", "block", workflowInfo = WorkflowInfo("", "")),
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", workflowInfo = WorkflowInfo("", "")),
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

    @Test
    fun testStringNotContains() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'String contains' test contains 'test1', 'test2', 'test3', 'test4' return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf("test" to "not contains")
            )
        )
    }

    @Test
    fun `given an existing substring must go ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'String contains' test_property contains 'test1', 'test2', 'test3', 'test4' return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "String contains", risk = "block", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf("test_property" to "this is a test3")
            )
        )
    }
    @Test

    fun `given a null collection when processing rule then returns OK`() {

        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rappi_user_avg_gte_0_5' order.items.average { type = 'a' } > 0.5 return block default allow
            end
            """

        val result = WorkflowEvaluator(workflow).evaluate(mapOf("key" to "value"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", warnings = setOf("order field cannot be found"), workflowInfo = WorkflowInfo("", "")), result)
    }

    @Test

    fun `given a null string field when comparing must return false for the rule`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'String contains' test = 'test1' return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)

        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", warnings = setOf("test field cannot be found"), workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf("test2" to "test3")
            )
        )
    }

    @Test
    fun `given a null value when applying date diff must return false`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' dateDiff(day, currentDate(), createdAt) > 0 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", risk = "allow", ruleSet = "default", rule = "default",  warnings = setOf("createdAt field cannot be found"), workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "not_createdAt" to 1
                )
            )
        )
    }


    @Test
    fun `given a null value when applying date diff comparation must return false`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' dateDiff(day, currentDate(), createdAt) + 15  > 15 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default", risk = "allow", warnings = setOf("createdAt field cannot be found"), workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "not_createdAt" to 1
                )
            )
        )
    }

    @Test
    fun `given a null value when comparing must return false`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' createdAt > 0 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test",  ruleSet = "default", rule = "default", risk = "allow", warnings = setOf("createdAt field cannot be found"), workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "not_createdAt" to 1
                )
            )
        )
    }

    @Test
    fun `given any workflow value when evaluating null must return false`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'registration_attempts' createdAt > 0 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", warnings = setOf("createdAt field cannot be found"), workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf()
            )
        )
    }

    @Test
    fun `math greater within precision works`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'test_a' score > 0.04 return block
                    default allow
                end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "test_a", risk = "block", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "score" to 0.041
                )
            )
        )
    }
    @Test
    fun `math greater out of precision fails`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'test_a' score > 0.04 return block
                    default allow
                end
        """

        val ruleEngine = WorkflowEvaluator(workflow)

        Assertions.assertEquals(
            WorkflowResult(workflow="test", ruleSet = "default", rule = "default", risk = "allow", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(mapOf(
                "score" to 0.0400000000001
            ))
        )
    }

    @Test
    fun `math lower within precision works`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'test_a' score < 0.04 return block
                    default allow
                end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "test_a", risk = "block", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "score" to 0.03999999999
                )
            )
        )
    }

    @Test
    fun `math lower out of precision fails`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'test_a' score < 0.04 return block
                    default allow
                end
        """

        val ruleEngine = WorkflowEvaluator(workflow)

        Assertions.assertEquals(
            WorkflowResult(workflow="test", ruleSet = "default", rule = "default", risk = "allow", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(mapOf(
                "score" to 0.0400000000001
            ))
        )
    }

    @Test
    fun `math equal within precision works`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'test_a' score = 0.04 return block
                    default allow
                end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow="test", ruleSet = "default", rule = "default", risk = "allow", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "score" to 0.0400000001
                )
            )
        )
    }

    @Test
    fun `math equal out of precision fails`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'test_a' score = 0.04 return block
                    default allow
                end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "test_a", risk = "block", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(mapOf(
                "score" to 0.0400000000000000002
            ))
        )
    }

    @Test
    fun `eq_ic is case insensitive`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'test' test.any { a = 'A' } return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "test",  risk = "block", warnings = setOf(), workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "test" to listOf(
                        mapOf(
                            "a" to "a"
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `eq is case sensitive`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'test' test.any { a == 'A' } return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", warnings = setOf(), workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf(
                    "test" to listOf(
                        mapOf(
                            "a" to "a"
                        )
                    )
                )
            )
        )
    }

    @Test
    fun testAbsoluteValueNegativeNumber() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'absolute_value' abs(test) = 1 AND abs(test) <> -1 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "absolute_value",  risk = "block", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf("test" to -1)
            )
        )
    }

    @Test
    fun testAbsoluteValueWithString() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'absolute_value' abs(test) = -1 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf("test" to "something")
            )
        )
    }

    @Test
    fun testAbsoluteValueWithPlusOperation() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'absolute_value' abs(3+3) = 6 return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "absolute_value",  risk = "block", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf("test" to "something")
            )
        )
    }

    @Test
    fun testAbsoluteValueWithExponential() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'absolute_value' abs(test) = 1e-7 return block
                default allow
            end
        """
        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "absolute_value",  risk = "block", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf("test" to -1e-7)
            )
        )
    }

    @Test
    fun testAbsoluteValueWithGreaterThan() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'absolute_value' abs(4>3) = 1 return block
                default allow
            end
        """
        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", workflowInfo = WorkflowInfo("", "")),
            ruleEngine.evaluate(
                mapOf("test" to 1)
            )
        )
    }
}

