import com.github.iamrenny.ruleflow.Workflow
import com.github.iamrenny.ruleflow.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BooleanTest {
    @Test
    fun `given a boolean expression when operating an AND operation must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x AND y OR z return block
                default allow
            end
        """

        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult( "test", "default", "default", "allow"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to false,
                    "y" to true,
                    "z" to false
                )
            )
        )
    }

    @Test
    fun `given a boolean expression when operating AND with constant must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x AND y OR false  = false return block
                default allow
            end
        """

        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to true,
                    "y" to true
                )
            )
        )
    }


    @Test
    fun `given a boolean expression when using parenthesis then inner expression must be resolved first`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' (x OR y) AND z return block
                default allow
            end
        """

        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to true,
                    "y" to false,
                    "z" to true
                )
            )
        )
    }

    @Test
    fun `given a complex boolean expression when operating must be resolved ok`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x OR y OR false AND z  return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "x" to true,
                    "y" to false,
                    "z" to true
                )
            )
        )
    }


    @Test
    fun `given a boolean expression when operating with a non boolean value must result in a warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x AND 'false' return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        val evaluation = ruleEngine.evaluate(
            mapOf(
                "x" to true
            )
        )
        Assertions.assertTrue(
            evaluation.warnings.isNotEmpty()
        )
    }

    @Test
    fun `given an expression with a non existant field when operating must return a warning`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' x AND y return block
                default allow
            end
        """
        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        val evaluation = ruleEngine.evaluate(
            mapOf(
                "x" to true
            )
        )
        Assertions.assertEquals(
            setOf("y field cannot be found"),
            evaluation.warnings
        )
        Assertions.assertEquals("allow", evaluation.result)
    }


    @Test
    fun `test`(){
        val workflow = """
             workflow 'test'
                ruleset 'dummy'
                'test' order.custom.user_is_prime = 'true'
             and features.is_card_bin_in_anomaly_detector <> 'true' 
             and features.fake_users_user_email_score <= 1 
             and user.email_verification_status in 'VERIFIED' 
             and (features.mk_payer_distinct_cc_fingerprint_90d + features.mk_payer_distinct_device_id_90d) <= 8 
             and features.crosses_login_device_qty_users_7d <= 3 and features.crosses_registration_device_qty_users_7d < 1 
             and (features.user_compensations_amount_90d/features.mk_payer_approved_amount_90d) < 0.05 
             and features.mk_payer_approved_qty_7d >= 1 and features.mk_payer_approved_qty_30d >= 6 
             and (features.mk_payer_approved_qty_60d >= features.mk_payer_approved_qty_30d + 6) 
             and (features.mk_payer_approved_qty_90d >= features.mk_payer_approved_qty_60d + 6) 
             and features.mk_payer_approved_amount_30d >= 50 
             and (features.mk_payer_approved_amount_90d >= features.mk_payer_approved_amount_30d + 100) return block
             default allow
             end
        """.trimIndent()


        val ruleEngine = com.github.iamrenny.ruleflow.Workflow(workflow)
        val evaluation = ruleEngine.evaluate(
            mapOf(
                "order" to mapOf("custom" to mapOf("user_is_prime" to true)),
                "features" to mapOf(
                    "is_card_bin_in_anomaly_detector" to true,
                    "mk_payer_distinct_cc_fingerprint_90d" to 0,
                    "mk_payer_distinct_device_id_90d" to 0,
                    "fake_users_user_email_score" to 0,
                    "user_compensations_amount_90d" to 0,
                    "mk_payer_approved_amount_90d" to 0,
                    "mk_payer_approved_qty_90d" to 0
                )
            )
        )
        Assertions.assertEquals(
            setOf<String>(),
            evaluation.warnings
        )
        Assertions.assertEquals("allow", evaluation.result)
    }
}
