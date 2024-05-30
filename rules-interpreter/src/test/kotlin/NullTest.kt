import com.github.iamrenny.rulesflow.Workflow
import com.github.iamrenny.rulesflow.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NullTest {
    @Test
    fun `given an expression and a null value when operating should set a warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x + y * z  = 15 return block
                default allow
            end
        """

        val ruleEngine = Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "default", "default", "allow", setOf(), setOf("x field cannot be found")),
            ruleEngine.evaluate(
                mapOf(
                    "y" to 22,
                    "z" to 0
                )
            )
        )
    }

    @Test
    fun `given an expression with a null value when operating should not set a warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x = null AND x + y * z  = 15 return block
                default allow
            end
        """

        val ruleEngine = Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block", setOf()),
            ruleEngine.evaluate(
                mapOf(
                    "y" to 22,
                    "z" to 0
                )
            )
        )
    }
}