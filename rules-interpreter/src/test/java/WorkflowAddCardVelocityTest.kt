import com.github.iamrenny.ruleflow.Workflow
import com.github.iamrenny.ruleflow.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration
import java.time.LocalDateTime

class WorkflowAddCardVelocityTest {

    companion object {

        private var engine: com.github.iamrenny.ruleflow.Workflow? = null

        private val ONE_DAY = Duration.ofDays(1)
        private val ONE_WEEK = Duration.ofDays(7)

        private val DEBIT_CARD_ADDED_TODAY = mapOf("payment_type" to "debit_card", "created_at" to LocalDateTime.now().toString())
        private val CREDIT_CARD_ADDED_TODAY = mapOf("payment_type" to "credit_card", "created_at" to LocalDateTime.now())
        private val CREDIT_CARD_ADDED_LAST_7_DAYS = mapOf("payment_type" to "credit_card", "created_at" to LocalDateTime.now()
                .minusSeconds(ONE_DAY.seconds * 2)
                .toString())
        private val CREDIT_CARD_ADDED_LAST_30_DAYS = mapOf("payment_type" to "credit_card", "created_at" to LocalDateTime.now()
                .minusSeconds(ONE_WEEK.seconds * 2)
                .toString())

        @JvmStatic
        @Suppress("UNUSED")
        fun rules(): Array<Arguments> {
            return arrayOf(
                    // Allow
                    Arguments.of(
                            mapOf("payment_methods" to listOf(
                                    DEBIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY
                            )),
                        WorkflowResult(
                            workflow = "add_card",
                            ruleSet = "default",
                            rule = "default",
                            result = "allow")
                    ),

                    // Has more than allowed per day
                    Arguments.of(
                            mapOf("payment_methods" to listOf(
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_TODAY
                            )),
                            WorkflowResult(
                                    workflow = "add_card",
                                    ruleSet = "Registration velocity",
                                    rule = "Has more than allowed per day",
                                    result = "block"
                                    )
                    ),

                    // Has more than allowed per week
                    Arguments.of(
                            mapOf("payment_methods" to listOf(
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS,
                                    CREDIT_CARD_ADDED_LAST_7_DAYS
                            )),
                            WorkflowResult(
                                    workflow = "add_card",
                                    ruleSet = "Registration velocity",
                                    rule = "Has more than allowed per week",
                                    result = "block"
                                    )
                    ),

                    // Has more than allowed per week
                    Arguments.of(
                            mapOf("payment_methods" to listOf(
                                    CREDIT_CARD_ADDED_TODAY,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS,
                                    CREDIT_CARD_ADDED_LAST_30_DAYS
                            )),
                            WorkflowResult(
                                    workflow = "add_card",
                                    ruleSet = "Registration velocity",
                                    rule = "Has more than allowed per month",
                                    result = "block"
                                    )
                    )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("rules")
    fun test(data: Map<String, Any>, expected: WorkflowResult) {
        val workflow = javaClass.classLoader
                .getResourceAsStream("samples/workflows/test_add_card.RuleFlowLanguage")!!.reader().readText()
        val actual = getEngine(workflow).evaluate(data)
        Assertions.assertEquals(expected, actual)
    }

    private fun getEngine(workflow: String): com.github.iamrenny.ruleflow.Workflow {
        if (engine == null) {
            engine = com.github.iamrenny.ruleflow.Workflow(workflow)
        }
        return engine!!
    }

}