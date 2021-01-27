package com.rappi.fraud.rules.parser.conditions

import com.rappi.fraud.rules.parser.WorkflowEvaluator
import com.rappi.fraud.rules.parser.vo.WorkflowInfo
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PropertyConditionUnitTest {
    @Test
    fun `given a workflow with undefined conditions when evaluating must return exception`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' user_id = 15 return block with action('manual_review')
                default allow
            end
        """
        val request = mapOf("not_a_user_id" to 10)
        val result = WorkflowEvaluator(workflow).evaluate(request)
        assertEquals(
            WorkflowResult(
                workflow = "test",
                ruleSet = "default",
                rule = "default",
                risk = "allow",
                actions = setOf(),
                actionsWithParams = mapOf(),
                workflowInfo = WorkflowInfo("", ""),
                warnings = setOf("user_id field cannot be found")
            ), result)
    }
}