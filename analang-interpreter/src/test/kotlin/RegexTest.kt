import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.vo.WorkflowEvaluatorResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RegexTest {

    @Test
    fun `given an expression with a valid string field when matching regex must return true`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' regex_strip(x, '^(0|1+0+)+') = '3223863467' return block
                default allow
            end
        """
        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult("test", "dummy", "item_a", "block", warnings = setOf()),
            ruleEngine.evaluate(
                mapOf(
                    "x" to "01110000003223863467"
                )
            )
        )
    }

    @Test
    fun `given an expression with same string value when matching regex must return true`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' regex_strip(x, '^(0|1+0+)+') = '3223863467' return block
                default allow
            end
        """
        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult("test", "dummy", "item_a", "block", warnings = setOf()),
            ruleEngine.evaluate(
                mapOf(
                    "x" to "3223863467"
                )
            )
        )
    }

    @Test
    fun `given an expression with same string value when not matching regex must return false`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' regex_strip(x, '^4333') = '3223863467' return block
                default allow
            end
        """
        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowEvaluatorResult("test", "default", "default", "allow", warnings = setOf()),
            ruleEngine.evaluate(
                mapOf(
                    "x" to "01110000003223863467"
                )
            )
        )
    }
}