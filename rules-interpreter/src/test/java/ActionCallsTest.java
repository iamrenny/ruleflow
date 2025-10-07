import io.github.iamrenny.ruleflow.vo.WorkflowResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ActionCallsTest {

    @Test
    public void given_multimatch_workflow_with_three_matching_rules_must_have_correct_actioncalls() {
        String workflow = """
            workflow 'multimatch_test'
                evaluation_mode multi_match
                ruleset 'security'
                    'rule_1' user_id = 15 return block with action('manual_review', {'rule': 'rule_1'})
                    'rule_2' user_id = 15 return block with action('logout_user', {'rule': 'rule_2'})
                    'rule_3' user_id = 15 return block with action('notify_admin', {'rule': 'rule_3'})
                default allow
            end
        """;
        Map<String, Object> request = Map.of("user_id", 15);
        WorkflowResult result = new io.github.iamrenny.ruleflow.Workflow(workflow).evaluate(request);
        
        // Verify main result has actionCalls from first matched rule only
        Assertions.assertEquals(1, result.getActionCalls().size());
        Assertions.assertEquals("manual_review", result.getActionCalls().get(0).getName());
        
        // Verify matchedRules has actionCalls for all 3 rules
        Assertions.assertEquals(3, result.getMatchedRules().size());
        Assertions.assertEquals("manual_review", result.getMatchedRules().get(0).getActionCalls().get(0).getName());
        Assertions.assertEquals("logout_user", result.getMatchedRules().get(1).getActionCalls().get(0).getName());
        Assertions.assertEquals("notify_admin", result.getMatchedRules().get(2).getActionCalls().get(0).getName());
    }
}