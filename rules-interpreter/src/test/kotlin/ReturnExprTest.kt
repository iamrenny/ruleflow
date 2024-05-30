import com.github.iamrenny.rulesflow.Workflow
import com.github.iamrenny.rulesflow.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ReturnExprTest {
    @Test
    fun `given a rule when returning a value must return x`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x + y * z  = 15 return expr(x)
                default allow
            end
        """

        val ruleEngine = Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "15"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 15,
                    "y" to 22,
                    "z" to 0
                )
            )
        )
    }
    @Test
    fun `given an expression with a valid expression when returning must calculate value`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' bucket = '4' return expr(courier_model_response.score * city_features.percentile_q1)
                default 0
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


    @Test
    fun `given a rule when returning an expr must return correct value`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x + y * z  = 15 return expr(x + y)
                default allow
            end
        """

        val ruleEngine = Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "37.00"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 15,
                    "y" to 22,
                    "z" to 0
                )
            )
        )
    }
}