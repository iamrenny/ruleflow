import io.github.iamrenny.ruleflow.vo.WorkflowResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ActionsTest {

    @Test
    public void given_workflow_with_actions_without_params_must_return_valid_response() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review')
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of("manual_review", Map.of())
        );
        assertWorkflowResult(expectedResult, result);
    }

    @Test
    public void given_workflow_with_actions_name_wildcard_must_return_valid_response() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with manual_review
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of("manual_review", Map.of())
        );
        assertWorkflowResult(expectedResult, result);
    }

    @Test
    public void given_workflow_with_actions_without_explicit_params_must_return_valid_response() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review')
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of("manual_review", Map.of())
        );
        assertWorkflowResult(expectedResult, result);
    }

    @Test
    public void given_workflow_with_two_rules_must_return_ok() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review') and action('logout_user')
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of("manual_review", Map.of(), "logout_user", Map.of())
        );
        assertWorkflowResult(expectedResult, result);
    }

    @Test
    public void given_two_actions_one_with_params_one_without_params_must_return_valid_response() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review', {'test': 'me', 'foo': 'bar'}) and action('logout_user')
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of(
                "manual_review", Map.of("test", "me", "foo", "bar"),
                "logout_user", Map.of()
            )
        );
        assertWorkflowResult(expectedResult, result);
    }

    @Test
    public void given_action_with_params_must_return_valid_response() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review', {'test': 'me', 'foo': 'bar'})
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of("manual_review", Map.of("test", "me", "foo", "bar"))
        );
        assertWorkflowResult(expectedResult, result);
    }

    @Test
    public void given_action_with_params_using_request_values_must_return_valid_response() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review', {'test': 'me', 'userId': user_id})
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
                "test", "dummy", "rule_a", "block",
                Map.of("manual_review", Map.of("test", "me", "userId", "15"))
        );
        assertWorkflowResult(expectedResult, result);
    }



    @Test
    public void given_action_with_wildcard_name_and_params_must_return_valid_response() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with manual_review({'test': 'me', 'foo': 'bar'})
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of("manual_review", Map.of("test", "me", "foo", "bar"))
        );
        assertWorkflowResult(expectedResult, result);
    }

    @Test
    public void given_two_actions_with_wildcard_names_and_params_must_return_valid_response() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with manual_review({'test': 'me', 'foo': 'bar'}) AND apply_restriction({'responsible': 'homer'})
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of(
                "manual_review", Map.of("test", "me", "foo", "bar"),
                "apply_restriction", Map.of("responsible", "homer")
            )
        );
        assertWorkflowResult(expectedResult, result);
    }

    @Test
    public void given_two_actions_with_same_name_both_must_be_in_actions_list() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with apply_restriction({'test': 'me', 'foo': 'bar'}) AND apply_restriction({'responsible': 'homer'})
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        WorkflowResult expectedResult = new WorkflowResult(
            "test", "dummy", "rule_a", "block",
            Map.of("apply_restriction", Map.of("responsible", "homer", "test", "me", "foo", "bar"))
        );
        assertWorkflowResult(expectedResult, result);
    }

    private void assertWorkflowResult(WorkflowResult expected, WorkflowResult actual) {
        Assertions.assertEquals(expected.getWorkflow(), actual.getWorkflow());
        Assertions.assertEquals(expected.getRuleSet(), actual.getRuleSet());
        Assertions.assertEquals(expected.getRule(), actual.getRule());
        Assertions.assertEquals(expected.getResult(), actual.getResult());
        Assertions.assertEquals(expected.getActions(), actual.getActions());
        Assertions.assertEquals(expected.getActionsWithParams(), actual.getActionsWithParams());
    }
}