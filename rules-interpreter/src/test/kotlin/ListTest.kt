import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ListTest {
    @Test
    fun testListAny() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "order" to mapOf(
                        "items" to listOf(
                            mapOf("type" to "c"),
                            mapOf("type" to "b"),
                            mapOf("type" to "a")
                        )
                    )
                )
            )
        )
    }

    @Test
    fun testListAnyMissing() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", risk = "allow"), ruleEngine.evaluate(
                mapOf(
                    "order" to mapOf(
                        "items" to listOf(
                            mapOf("type" to "c"),
                            mapOf("type" to "b"),
                            mapOf("type" to "e")
                        )
                    )
                )
            )
        )
    }

    @Test
    fun testListAnyNotCollection() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """


        val result = RuleEngine(workflow).evaluate(mapOf("items" to "1"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", risk = "allow"), result)

    }

    @Test
    fun testListAll() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' items.all { type = 'a' } return block
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult("test", "dummy", "item_a", "block"),
            ruleEngine.evaluate(
                mapOf(
                    "items" to listOf(
                        mapOf("type" to "a"),
                        mapOf("type" to "a"),
                        mapOf("type" to "a")
                    )
                )
            )
        )
    }

    @Test
    fun testListAllMissing() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' items.all { type = 'a' } return allow
                default allow
            end
        """

        val ruleEngine = RuleEngine(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", risk = "allow"), ruleEngine.evaluate(
                mapOf(
                    "items" to listOf(
                        mapOf("type" to "b"),
                        mapOf("type" to "a"),
                        mapOf("type" to "a")
                    )
                )
            )
        )
    }

    @Test
    fun testListAllNotCollection() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.all { type = 'a' } return block
                default allow
            end
        """

        val result = RuleEngine(workflow).evaluate(mapOf("items" to 1))
        Assertions.assertEquals(WorkflowResult(workflow = "test", risk = "allow"), result)
    }

    @Test
    fun `given a list contains a value when evaluating must return true`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' item in 'item', 'item2', 'item3' return block
                default allow
            end
        """
        val result = RuleEngine(workflow).evaluate(mapOf("item" to "item"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "item_a", risk = "block"), result)

    }

    @Test
    fun `given a single item list when evaluating must return true`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' item in 'item' return block
                default allow
            end
        """
        val result = RuleEngine(workflow).evaluate(mapOf("item" to "item"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "item_a", risk = "block"), result)
    }


    @Test
    fun `given a list when evaluating a non existent value must return false`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a'  string in 'item' return block
                default allow
            end
        """
        val result = RuleEngine(workflow).evaluate(mapOf("string" to "item"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a list that contains a value when evaluating a nested property must return true`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.payment_method.card_bin in 'item', 'item2', 'item3' return block
                default allow
            end
        """
        val result = RuleEngine(workflow).evaluate(mapOf("order" to mapOf("payment_method" to mapOf("card_bin"  to "item"))))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "item_a", risk = "block"), result)

    }

    @Test
    fun `given a list when evaluating a non existent property must return false`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' item in 'item' return block
                default allow
            end
        """
        val result = RuleEngine(workflow).evaluate(mapOf("item2" to "item"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", risk = "allow"), result)
    }

    @Test
    fun `given a list property when evaluating a non existent value must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a'  string in card_bins return block
                     default allow
            end
        """
        val result = RuleEngine(workflow).evaluate(mapOf("string" to "item", "card_bins" to listOf("item1", "item2", "item")))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a nested property when evaluating must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a'  string in smart_lists.card_bins return block
                default allow
            end
        """
        val result = RuleEngine(workflow).evaluate(mapOf("string" to "item", "smart_lists" to mapOf("card_bins" to listOf("item1", "item2", "item"))))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given an empty nested property when evaluating must return default value`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a'  string in smart_lists.card_bins return block
                default allow
            end
        """
        val result = RuleEngine(workflow).evaluate(mapOf("string" to "item", "card_bin" to listOf("item1", "item2", "item")))
        Assertions.assertEquals(WorkflowResult(workflow = "test", risk = "allow"), result)
    }
}