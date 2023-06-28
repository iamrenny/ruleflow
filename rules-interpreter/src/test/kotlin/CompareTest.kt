import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.vo.WorkflowEvaluatorResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CompareTest {

    @Test
    fun `given a string when operating lt operation must go ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'x_is_lesser' x < '7.53'  return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult( "test", "dummy", "x_is_lesser", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to "7.52.2.19911"
                )
            )
        )
    }

    @Test
    fun `given a string when operating lte operation must go ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'x_is_lesser' x <= '7.53'  return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult( "test", "dummy", "x_is_lesser", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to "7.52.2.19911"
                )
            )
        )
    }
    @Test
    fun `given a string when operating gt operation must go ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'x_greater' x > '7.53'  return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult( "test", "dummy", "x_greater", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to "7.53.2.19911"
                )
            )
        )
    }

    @Test
    fun `given a string when operating gte operation must go ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'x_greater' x >= '7.53'  return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult( "test", "dummy", "x_greater", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to "7.53.2.19911"
                )
            )
        )
    }
}