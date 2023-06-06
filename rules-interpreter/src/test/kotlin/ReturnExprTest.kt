import com.rappi.analang.Workflow
import com.rappi.analang.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ReturnExprTest {
        @Test
        fun `given an expression with a valid expression when returning must calculate value`() {
            val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' bucket = '4' return courier_model_response.score * city_features.percentile_q1
                default allow
            end
        """
            val ruleEngine = Workflow(workflow)
            Assertions.assertEquals(
                WorkflowResult("test", "dummy", "item_a", "2.0000", warnings = setOf()),
                ruleEngine.evaluate(
                    mapOf(
                        "bucket" to "4",
                        "courier_model_response" to mapOf("score" to 0.5),
                        "city_features" to mapOf("percentile_q1" to 4)
                    )
                )
            )
        }
}