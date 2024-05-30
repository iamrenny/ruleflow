package com.rappi.fraud.rules.parser.evaluators.context

import com.github.iamrenny.rulesflow.Workflow
import com.github.iamrenny.rulesflow.vo.WorkflowResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PropertyContextEvaluatorUnitTest {
    @Test
    fun `given a workflow with undefined property when evaluating must return exception`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review')
                default allow
            end
        """
        val request = mapOf("not_a_user_id" to 10)
        val result = Workflow(workflow).evaluate(request)
        assertEquals(
            WorkflowResult(
                workflow = "test",
                ruleSet = "default",
                rule = "default",
                result = "allow",
                actions = setOf(),
                actionsWithParams = mapOf(),
                warnings = setOf("user_id field cannot be found")
            ), result)
    }

    @Test
    fun `given a workflow with a property when evaluating must return root value`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block
                default allow
            end
        """
        val request = mapOf("user_id" to 15)
        val result = Workflow(workflow).evaluate(request)
        assertEquals(
            WorkflowResult(
                workflow = "test",
                ruleSet = "dummy",
                rule = "rule_a",
                result = "block",
                actions = setOf(),
                actionsWithParams = mapOf(),
                warnings = setOf()
            ), result)
    }

    @Test
    fun `given a workflow with existing root and inner field with exact name must compare with root value`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' request.orders.any { compare_order_id = .request.order_id } return block
                default allow
            end
        """
        val request = mapOf(
            "request" to mapOf(
                "order_id" to "A",
                "orders" to
                        listOf(
                            mapOf(
                                "order_id" to "B",
                                "compare_order_id" to "A"
                            )
                        )
            )

        )
        val result = Workflow(workflow).evaluate(request)
        assertEquals(
            WorkflowResult(
                workflow = "test",
                ruleSet = "dummy",
                rule = "rule_a",
                result = "block",
                actions = setOf(),
                actionsWithParams = mapOf(),
                warnings = setOf()
            ), result)
    }

    @Test
    fun `given a workflow with existing root and inner field with exact name when not accessing from root must take inner field `() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' request.orders.any { compare_order_id = order_id } return block
                default allow
            end
        """
        val request = mapOf(
            "order_id" to "A",
            "request" to mapOf(
                "orders" to
                        listOf(
                            mapOf(
                                "order_id" to "B",
                                "compare_order_id" to "A"
                            )
                        )
            )

        )
        val result = Workflow(workflow).evaluate(request)
        assertEquals(
            WorkflowResult(
                workflow = "test",
                ruleSet = "default",
                rule = "default",
                result = "allow",
                actions = setOf(),
                actionsWithParams = mapOf(),
                warnings = setOf()
            ), result)
    }


    @Test
    fun `given a workflow with non existing root value when accessing with root accessor must fail`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' request.orders.any { compare_order_id = .request.order_id } return block
                default allow
            end
        """
        val request = mapOf(
            "request" to mapOf(
                "orders" to
                        listOf(
                            mapOf(
                                "order_id" to "B",
                                "compare_order_id" to "A"
                            )
                        )
            )

        )
        val result = Workflow(workflow).evaluate(request)

        assertEquals(
            WorkflowResult(
                workflow = "test",
                ruleSet = "default",
                rule = "default",
                result = "allow",
                actions = setOf(),
                actionsWithParams = mapOf(),
                warnings = setOf("order_id field cannot be found")
            ), result)
    }
}