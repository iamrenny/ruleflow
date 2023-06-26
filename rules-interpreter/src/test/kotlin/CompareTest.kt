import com.rappi.analang.Workflow
import com.rappi.analang.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CompareTest {

    @Test
    fun `given a string when operating lt operation must go ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x > '7.53'  return block
                default allow
            end
        """

        val ruleEngine = Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult( "test", "default", "default", "allow"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to "7.53.2.19911"
                )
            )
        )
    }
}