import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.vo.WorkflowEvaluatorResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class WorkflowEvaluatorRappiPayPrepaidTest {

    companion object {

        private var engine: WorkflowEvaluator? = null

        @JvmStatic
        @Suppress("UNUSED")
        fun rules(): Array<Arguments> {
            return arrayOf(
                // Not purchase
                Arguments.of(
                    "samples/data/prepaid_not_purchase.json",
                    WorkflowEvaluatorResult(workflow = "rappi_pay_prepaid", result = "allow", ruleSet = "default", rule = "default")
                ),

                // Hard Cap
                Arguments.of(
                    "samples/data/prepaid_hard_cap_amt_1d.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap", "amt_1d", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_hard_cap_amt_7d.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap", "amt_7d", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_hard_cap_amt_transaction.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap", "amt_transaction", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_hard_cap_qty_1d.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap", "qty_1d", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_hard_cap_qty_6h.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap", "qty_6h", "block")
                ),

                // Card not present
                Arguments.of(
                    "samples/data/prepaid_card_not_present_amt_transaction.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "card_not_present_hard_cap", "amt_transaction", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_card_not_present_qty_1d.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "card_not_present_hard_cap", "qty_1d", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_card_not_present_qty_6h.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "card_not_present_hard_cap", "qty_6h", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_card_not_present_amt_1d.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "card_not_present_hard_cap", "amt_1d", "block")
                ),

                // Hard Cap International
                Arguments.of(
                    "samples/data/prepaid_hard_cap_int_amt_1d.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap_international", "amt_1d", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_hard_cap_int_amt_7d.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap_international", "amt_7d", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_hard_cap_int_amt_transaction.json",
                    WorkflowEvaluatorResult(
                        "rappi_pay_prepaid",
                        "vector_rule_hard_cap_international",
                        "amt_transaction",
                        "block"
                    )
                ),
                Arguments.of(
                    "samples/data/prepaid_hard_cap_int_qty_1d.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap_international", "qty_1d", "block")
                ),
                Arguments.of(
                    "samples/data/prepaid_hard_cap_int_qty_6h.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule_hard_cap_international", "qty_6h", "block")
                ),

                // Velocity
                Arguments.of(
                    "samples/data/prepaid_velocity.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule", "velocity", "block")
                ),

                // High Rejected Ratio
                Arguments.of(
                    "samples/data/prepaid_high_rejected_ratio.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "vector_rule", "high_rejected_ratio", "block")
                ),

                // Skimming Counterfeit Card
                Arguments.of(
                    "samples/data/prepaid_skimming_counterfeit_card.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "skimming_counterfeit_card", "deny", "block")
                ),

                // Fraudulent funds
                Arguments.of(
                    "samples/data/prepaid_copied_card_cash_out_of_fraudulent_funds.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "copied_card_cash_out_of_fraudulent_funds", "deny", "block")
                ),

                // Potentially copied card foreign withdraw
                Arguments.of(
                    "samples/data/prepaid_potentially_copied_card_foreign_withdraw.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "potentially_copied_card_foreign_withdraw", "deny", "block")
                ),

                // Potentially copied card
                Arguments.of(
                    "samples/data/prepaid_potentially_copied_card.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "potentially_copied_card", "deny", "block")
                ),

                // Decline attempts when above the set limit
                Arguments.of(
                    "samples/data/prepaid_decline_attempts_when_above_the_set_limit.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "decline_attempts_when_above_the_set_limit", "deny", "block")
                ),

                // Decline attempts when above the set limit
                Arguments.of(
                    "samples/data/prepaid_high_risk_location_rule.json",
                    WorkflowEvaluatorResult("rappi_pay_prepaid", "high_risk_location_rule", "deny", "block")
                ),

                // Allow
                Arguments.of(
                    "samples/data/prepaid_allow.json",
                    WorkflowEvaluatorResult(workflow = "rappi_pay_prepaid", result = "allow", ruleSet = "default", rule = "default")
                )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("rules")
    fun test(dataPath: String, expected: WorkflowEvaluatorResult) {
        val workflow = getFileContent("samples/workflows/test_rappipay_prepaid.ANA")
        val data: Map<String, Any> =
            Gson().fromJson(getFileContent(dataPath), object : TypeToken<Map<String, Any>>() {}.type)
        val actual = getEngine(workflow).evaluate(data)
        Assertions.assertEquals(expected, actual)
    }

    private fun getEngine(workflow: String): WorkflowEvaluator {
        if (engine == null) {
            engine = WorkflowEvaluator(workflow)
        }
        return engine!!
    }

    private fun getFileContent(path: String): String =
        javaClass.classLoader.getResourceAsStream(path)!!.reader().readText()

}