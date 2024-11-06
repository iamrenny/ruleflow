import com.github.iamrenny.ruleflow.Workflow;
import com.github.iamrenny.ruleflow.vo.Action;
import com.github.iamrenny.ruleflow.vo.WorkflowResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

class RouterTest {

    @Test
    public void givenRoutingWorkflowWhenRoutingToDefaultCaseMustReturnValuesSuccessfully() {
        String workflow = """
          workflow 'providers'
          ruleset 'routing' 
               'Cybersourse1-rappi_master' user_id = '101000042449' return route with route({'provider':'cybersource', 'merchant_id': 'rappi_master2'})
               'Cybersourse2-rappi_master2' user_id = '151000033978'  return route with route({'provider': 'cybersource', 'merchant_id': 'rappi_master2'})
               'Cybersourse3-rappi_dm_bwl' user_id = '151000033979' return route with route({'provider': 'cybersource', 'merchant_id': 'rappi_dm_bwl'})
               'Cybersourse4-rappi_dm_rest_5a15_tkt' user_id = '151000033977' return route with route({'provider': 'cybersource', 'merchant_id': 'rappi_dm_rest_5a15_tkt'})
               'Riskified-rappi' user_id = '15100003391381823' return route with route({'provider': 'riskified', 'tag': 'rappi'})
               'Fraud_rules_engine' user_id = '151000033976' return route with route({'provider': 'fraud_rules_engine'})
           default route with route({'provider': 'fraud_rules_engine'})
           end
        """;

        Workflow ruleEngine = new Workflow(workflow);
        WorkflowResult expectedResult = new WorkflowResult(
            "providers",
            "default",
            "default",
            "route",
            Map.of("route", Map.of("provider", "fraud_rules_engine"))
        );

        WorkflowResult result = ruleEngine.evaluate(Map.of("user_id", "4"));

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void givenRoutingWorkflowWhenRoutingToSpecificRuleCaseMustReturnValue() {
        String workflow = """
          workflow 'router'
               ruleset 'providers' 
               'provider1' country in 'ar' return route with route({'provider':'provider1', 'merchant_id': 'master'})
               'provider2' country in 'us' return route with route({'provider': 'provider2', 'merchant_id': 'master2'})
               'provider3' user.type = 'premium' OR user_is_blacklisted = false return route with route({'provider': 'provider3', 'merchant_id': 'master3'})
               'provider4' order.vertical in 'restaurants', 'travel' return route with route({'provider': 'provider4', 'merchant_id': 'master4'})
           default do_not_route
           end
        """;

        Workflow ruleEngine = new Workflow(workflow);
        WorkflowResult expectedResult = new WorkflowResult(
            "router",
            "providers",
            "provider2",
            "route",
            Map.of("route", Map.of("provider", "provider2", "merchant_id", "master2"))
        );

        WorkflowResult result = ruleEngine.evaluate(Map.of("country", "us"));

        Assertions.assertEquals(expectedResult, result);
    }
}