import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.vo.WorkflowEvaluatorResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BooleanTest {
    @Test
    fun `given a boolean expression when operating an AND operation must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x AND y OR z return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult( "test", "default", "default", "allow"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to false,
                    "y" to true,
                    "z" to false
                )
            )
        )
    }

    @Test
    fun `given a boolean expression when operating AND with constant must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x AND y OR false  = false return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to true,
                    "y" to true
                )
            )
        )
    }


    @Test
    fun `given a boolean expression when using parenthesis then inner expression must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' (x OR y) AND z return block
                default allow
            end
        """

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to true,
                    "y" to false,
                    "z" to true
                )
            )
        )
    }

    @Test
    fun `given a complex boolean expression when operating must be resolved ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x OR y OR false AND z  return block
                default allow
            end
        """
        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to true,
                    "y" to false,
                    "z" to true
                )
            )
        )
    }


    @Test
    fun `given a boolean expression when operating with a non boolean value must result in a warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x AND 'false' return block
                default allow
            end
        """
        val ruleEngine = WorkflowEvaluator(workflow)
        val evaluation = ruleEngine.evaluate(
            mapOf(
                "x" to true
            )
        )
        Assertions.assertTrue(
            evaluation.warnings.isNotEmpty()
        )
    }

    @Test
    fun `given an expression with a non existant field when operating must return a warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x AND y return block
                default allow
            end
        """
        val ruleEngine = WorkflowEvaluator(workflow)
        val evaluation = ruleEngine.evaluate(
            mapOf(
                "x" to true
            )
        )
        Assertions.assertEquals(
            setOf("y field cannot be found"),
            evaluation.warnings
        )
        Assertions.assertEquals("allow", evaluation.risk)
    }
}
