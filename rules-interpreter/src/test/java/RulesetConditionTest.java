import io.github.iamrenny.ruleflow.Workflow;
import io.github.iamrenny.ruleflow.vo.WorkflowResult;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class RulesetConditionTest {
    @Test
    void rulesetConditionTrue_shouldEvaluateRules() {
        String workflow = """
            workflow 'test'
                ruleset 'main' 1 = 1 then
                    'rule1' (1 = 1 return yes)
                default return no
            end
        """;
        WorkflowResult result = new Workflow(workflow).evaluate(Map.of());
        assertEquals("yes", result.getResult());
    }

    @Test
    void rulesetConditionFalse_shouldSkipRules() {
        String workflow = """
            workflow 'test'
                ruleset 'main' 1 = 2 then
                    'rule1' (1 = 1 return yes)
                default return no
            end
        """;
        WorkflowResult result = new Workflow(workflow).evaluate(Map.of());
        assertEquals("no", result.getResult());
    }

    @Test
    void rulesetNoCondition_shouldEvaluateRules() {
        String workflow = """
            workflow 'test'
                ruleset 'main'
                    'rule1' (1 = 1 return yes)
                default return no
            end
        """;
        WorkflowResult result = new Workflow(workflow).evaluate(Map.of());
        assertEquals("yes", result.getResult());
    }
} 