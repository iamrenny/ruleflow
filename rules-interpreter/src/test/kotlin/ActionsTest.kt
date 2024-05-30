import com.github.iamrenny.rulesflow.Workflow
import com.github.iamrenny.rulesflow.vo.Action
import com.github.iamrenny.rulesflow.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ActionsTest {
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review"), actionsWithParams = mapOf("manual_review" to mapOf()),
            actionsList = listOf(Action("manual_review", emptyMap()))
        )
        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review"), actionsWithParams = mapOf("manual_review" to mapOf()),
            actionsList = listOf(Action("manual_review", emptyMap()))
        )
        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review"), actionsWithParams = mapOf("manual_review" to mapOf()),
            actionsList = listOf(Action("manual_review", emptyMap()))
        )
        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review", "logout_user"),
            actionsWithParams = mapOf("manual_review" to mapOf(), "logout_user" to mapOf()),
            actionsList = listOf(Action("manual_review", emptyMap()), Action("logout_user", emptyMap()))
        )
        Assertions.assertEquals(
            workflowResult, result
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review", "logout_user"),
            actionsWithParams = mapOf(
                "manual_review" to mapOf("test" to "me", "foo" to "bar"),
                "logout_user" to mapOf()
            ),
            actionsList = listOf(
                Action("manual_review", mapOf("test" to "me", "foo" to "bar")),
                Action("logout_user", emptyMap())
            )
        )
        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review"),
            actionsWithParams = mapOf("manual_review" to mapOf("test" to "me", "foo" to "bar")),
            actionsList = listOf(
                Action("manual_review", mapOf("test" to "me", "foo" to "bar"))
            )
        )
        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review"),
            actionsWithParams = mapOf("manual_review" to mapOf("test" to "me", "foo" to "bar")),
            actionsList = listOf(
                Action("manual_review", mapOf("test" to "me", "foo" to "bar"))
            )
        )
        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review"),
            actionsWithParams = mapOf("manual_review" to mapOf("test" to "me", "foo" to "bar")),
            actionsList = listOf(
                Action("manual_review", mapOf("test" to "me", "foo" to "bar"))
            )
        )
        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)
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
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test", ruleSet = "dummy", rule = "rule_a", result = "block",
            actions = setOf("manual_review", "apply_restriction"),
            actionsWithParams = mapOf(
                "manual_review" to mapOf("test" to "me", "foo" to "bar"),
                "apply_restriction" to mapOf("responsible" to "homer")
            ),
            actionsList = listOf(
                Action("manual_review", mapOf("test" to "me", "foo" to "bar")),
                Action("apply_restriction", mapOf("responsible" to "homer"))
            )
        )

        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)
    }

    @Test
    fun `given two actions with same name, both must be in actions list`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with apply_restriction({'test': 'me', 'foo': 'bar'}) AND apply_restriction({'responsible': 'homer'})
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = Workflow(workflow).evaluate(request)
        val workflowResult = WorkflowResult(
            workflow = "test",
            ruleSet = "dummy",
            rule = "rule_a",
            result = "block",
            actions = setOf("apply_restriction"),
            actionsWithParams = mapOf(
                "apply_restriction" to mapOf("responsible" to "homer")
            ),
            actionsList = listOf(
                Action("apply_restriction", mapOf("test" to "me", "foo" to "bar")),
                Action("apply_restriction", mapOf("responsible" to "homer"))
            )
        )
        Assertions.assertEquals(workflowResult.workflow, result.workflow)
        Assertions.assertEquals(workflowResult.ruleSet, result.ruleSet)
        Assertions.assertEquals(workflowResult.rule, result.rule)
        Assertions.assertEquals(workflowResult.result, result.result)
        Assertions.assertEquals(workflowResult.actions, result.actions)
        Assertions.assertEquals(workflowResult.actionsWithParams, result.actionsWithParams)
        Assertions.assertEquals(workflowResult.actionsList, result.actionsList)

    }
}