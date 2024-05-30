import com.github.iamrenny.rulesflow.Workflow
import com.github.iamrenny.rulesflow.vo.Action
import com.github.iamrenny.rulesflow.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RouterTest {

    @Test
    fun `given a routing workflow, when routing to default case must return values successfully`() {
        val workflow = """
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
        """
        val ruleEngine = Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult(
                "providers",
                "default",
                "default",
                "route",
                warnings = setOf(),
                actions = setOf("route"),
                actionsWithParams = mapOf("route" to mapOf("provider" to "fraud_rules_engine")),
                actionsList = listOf(Action("route", mapOf("provider" to "fraud_rules_engine")))
            ),
            ruleEngine.evaluate(
                mapOf(
                    "user_id" to "4"
                )
            )
        )
    }

    @Test
    fun `given a routing workflow, when routing to a specific rule case must return value`() {
        val workflow = """
          workflow 'router'
                   ruleset 'providers' 
                   'Cybersourse1-rappi_master' country in 'ar', 'cl', 'co', 'ec', 'pe' return route with route({'provider':'cybersource', 'merchant_id': 'rappi_master'})
                   'Cybersourse3-rappi_master2' country in 'br', 'cr', 'mx', 'uy' return route with route({'provider': 'cybersource', 'merchant_id': 'rappi_master2'})
                   'Cybersourse4-rappi_dm_bwl' user_is_whitelisted = true OR user_is_blacklisted = true return route with route({'provider': 'cybersource', 'merchant_id': 'rappi_dm_bwl'})
                   'Cybersourse4-rappi_dm_rest_5a15_tkt' order.vertical in 'restaurants', 'travel' return route with route({'provider': 'cybersource', 'merchant_id': 'rappi_dm_rest_5a15_tkt'})
           default do_not_route
           end
        """
        val ruleEngine = Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult(
                "router",
                "providers",
                "Cybersourse3-rappi_master2",
                "route",
                warnings = setOf(),
                actions = setOf("route"),
                actionsWithParams = mapOf("route" to mapOf("provider" to "cybersource", "merchant_id" to "rappi_master2")),
                actionsList = listOf(Action("route", mapOf("provider" to "cybersource", "merchant_id" to "rappi_master2")))
            ),
            ruleEngine.evaluate(
                mapOf(
                    "country" to "br"
                )
            )
        )
    }
}