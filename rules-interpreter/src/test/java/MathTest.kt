import com.github.iamrenny.ruleflow.Workflow
import com.github.iamrenny.ruleflow.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MathTest {
    @Test
    fun `given a math expression when operating a multiplication must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x + y * z  = 15 return block
                default allow
            end
        """

        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
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
    fun `given a math expression when operating multiplication with constant must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x + y * 0  = 15 return block
                default allow
            end
        """

        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 15,
                    "y" to 22
                )
            )
        )
    }


    @Test
    fun `given a math expression when using parenthesis then inner expression must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' (x + y) * z  = 80 return block
                default allow
            end
        """

        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 15,
                    "y" to 25,
                    "z" to 2
                )
            )
        )
    }

    @Test
    fun `given a math expression when multiplication expression must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x + y * z  = 65 return block
                default allow
            end
        """

        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 15,
                    "y" to 25,
                    "z" to 2
                )
            )
        )
    }

    @Test
    fun `given a complex math expression when operating must be resolved ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x + y - 3 * z  = -47.54 return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 3.43,
                    "y" to 0,
                    "z" to 16.99
                )
            )
        )
    }

    @Test
    fun `given a division  expression when operating must be resolved ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x / y = 2 return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 4,
                    "y" to 2
                )
            )
        )
    }

    @Test
    fun `given a division by zero expression when operating must result in a warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x / y = 2 return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "default", "default", "allow", warnings = setOf()),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 4,
                    "y" to 0
                )
            )
        )
    }

    @Test
    fun `given an expression with a non existant field when operating must return a warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x / y = 2 return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "default", "default", "allow", warnings = setOf("y field cannot be found")),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 4
                )
            )
        )
    }

    @Test
    fun `given a modulo  expression when operating must be resolved ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x % y = 9 return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 99,
                    "y" to 10
                )
            )
        )
    }

    @Test
    fun `given a modulo expression when operating must be not equal to expected result`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x % y = 9 return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "default", "default", "allow"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 99,
                    "y" to 100
                )
            )
        )
    }

    @Test
    fun `given a modulo expression when operating with non existing right value must return warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x % y = 9 return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "default", "default", "allow", warnings =  setOf("y field cannot be found")),
            ruleEngine.evaluate(
                mapOf(
                    "x" to 9
                )
            )
        )
    }

    @Test
    fun `given a modulo expression when operating with non existing left value must return warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x % y = 9 return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "default", "default", "allow", warnings =  setOf("x field cannot be found")),
            ruleEngine.evaluate(
                mapOf(
                    "y" to 10
                )
            )
        )
    }
}