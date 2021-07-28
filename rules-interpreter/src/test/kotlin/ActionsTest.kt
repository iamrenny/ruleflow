import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.vo.WorkflowInfo
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class
ActionsTest {
    @Test
    fun `given a workflow with actions without params must return valid response`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review')
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review"), actionsWithParams = mapOf("manual_review" to mapOf())
            ), result
        )
    }

    @Test
    fun `given a workflow with actions name wildcard must return valid response`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with manual_review
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review"), actionsWithParams = mapOf("manual_review" to mapOf())
            ), result
        )
    }

    @Test
    fun `given a workflow with actions without explicit params must return valid response`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review')
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review"), actionsWithParams = mapOf("manual_review" to mapOf())
            ), result
        )
    }

    @Test
    fun `given a workflow with actions with two rules must go ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review') and action('logout_user')
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review", "logout_user"),
                actionsWithParams = mapOf("manual_review" to mapOf(), "logout_user" to mapOf())
            ), result
        )
    }

    @Test
    fun `given two actions one with params, one without params must be returned in result`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review', {'test': 'me', 'foo': 'bar'}) and action('logout_user')
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review", "logout_user"),
                actionsWithParams = mapOf(
                    "manual_review" to mapOf("test" to "me", "foo" to "bar"),
                    "logout_user" to mapOf()
                )
            ), result
        )
    }

    @Test
    fun `given an action with params must be returned in result`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review',  {'test': 'me', 'foo': 'bar'})
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review"),
                actionsWithParams = mapOf("manual_review" to mapOf("test" to "me", "foo" to "bar"))
            ), result
        )
    }


    @Test
    fun `given an action with wildcard name and params must be returned in result`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with manual_review({'test': 'me', 'foo': 'bar'})
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review"),
                actionsWithParams = mapOf("manual_review" to mapOf("test" to "me", "foo" to "bar"))
            ), result
        )
    }

    @Test
    fun `given an action with wildcard name and without explicit params declaration must be returned in result`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with manual_review({'test': 'me', 'foo': 'bar'})
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review"),
                actionsWithParams = mapOf("manual_review" to mapOf("test" to "me", "foo" to "bar"))
            ), result
        )
    }

    @Test
    fun `given two action with wildcard names and params must be returned in result`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with manual_review({'test': 'me', 'foo': 'bar'}) AND apply_restriction({'responsible': 'homer'})
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        Assertions.assertEquals(
            WorkflowResult(
                workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block",
                actions = setOf("manual_review", "apply_restriction"),
                actionsWithParams = mapOf(
                    "manual_review" to mapOf("test" to "me", "foo" to "bar"),
                    "apply_restriction" to mapOf("responsible" to "homer")
                )
            ), result
        )
    }
}