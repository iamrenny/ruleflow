import io.github.iamrenny.ruleflow.vo.WorkflowResult;
import io.github.iamrenny.ruleflow.vo.Action;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;

class ActionCallsTest {

    @Test
    public void given_workflow_with_multiple_actions_same_name_should_store_separately_in_actionCalls() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with apply_restriction({'test': 'me', 'foo': 'bar'}) AND apply_restriction({'responsible': 'homer'})
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        
        // Verify the new actionCalls field contains both actions separately
        List<Action> actionCalls = result.getActionCalls();
        Assertions.assertEquals(2, actionCalls.size(), "Should have 2 separate action calls");
        
        // Find the actions by their parameters
        Action firstAction = actionCalls.stream()
            .filter(action -> action.getParams().containsKey("test") && action.getParams().containsKey("foo"))
            .findFirst()
            .orElse(null);
        Assertions.assertNotNull(firstAction, "First action should be found");
        Assertions.assertEquals("apply_restriction", firstAction.getName());
        Assertions.assertEquals(Map.of("test", "me", "foo", "bar"), firstAction.getParams());
        
        Action secondAction = actionCalls.stream()
            .filter(action -> action.getParams().containsKey("responsible"))
            .findFirst()
            .orElse(null);
        Assertions.assertNotNull(secondAction, "Second action should be found");
        Assertions.assertEquals("apply_restriction", secondAction.getName());
        Assertions.assertEquals(Map.of("responsible", "homer"), secondAction.getParams());
        
        // Verify backward compatibility - actionsWithParams should still merge them
        Map<String, Map<String, String>> actionsWithParams = result.getActionsWithParams();
        Assertions.assertEquals(1, actionsWithParams.size(), "Should have 1 merged action in actionsWithParams");
        Assertions.assertTrue(actionsWithParams.containsKey("apply_restriction"));
        Map<String, String> mergedParams = actionsWithParams.get("apply_restriction");
        Assertions.assertEquals(3, mergedParams.size(), "Should have 3 merged parameters");
        Assertions.assertEquals("me", mergedParams.get("test"));
        Assertions.assertEquals("bar", mergedParams.get("foo"));
        Assertions.assertEquals("homer", mergedParams.get("responsible"));
    }

    @Test
    public void given_workflow_with_different_actions_should_store_separately_in_actionCalls() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review', {'test': 'me'}) and action('logout_user', {'reason': 'suspicious'})
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        
        // Verify the new actionCalls field contains both actions separately
        List<Action> actionCalls = result.getActionCalls();
        Assertions.assertEquals(2, actionCalls.size(), "Should have 2 separate action calls");
        
        // Find the actions by their names
        Action manualReviewAction = actionCalls.stream()
            .filter(action -> "manual_review".equals(action.getName()))
            .findFirst()
            .orElse(null);
        Assertions.assertNotNull(manualReviewAction, "Manual review action should be found");
        Assertions.assertEquals(Map.of("test", "me"), manualReviewAction.getParams());
        
        Action logoutAction = actionCalls.stream()
            .filter(action -> "logout_user".equals(action.getName()))
            .findFirst()
            .orElse(null);
        Assertions.assertNotNull(logoutAction, "Logout action should be found");
        Assertions.assertEquals(Map.of("reason", "suspicious"), logoutAction.getParams());
        
        // Verify backward compatibility
        Map<String, Map<String, String>> actionsWithParams = result.getActionsWithParams();
        Assertions.assertEquals(2, actionsWithParams.size(), "Should have 2 separate actions in actionsWithParams");
        Assertions.assertTrue(actionsWithParams.containsKey("manual_review"));
        Assertions.assertTrue(actionsWithParams.containsKey("logout_user"));
    }
}
