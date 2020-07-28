import com.rappi.fraud.rules.parser.WorkflowEvaluator
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

        val ruleEngine = WorkflowEvaluator(workflow)
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", risk = "allow", ruleSet = "default", rule = "default"), ruleEngine.evaluate(
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


        val result = WorkflowEvaluator(workflow).evaluate(mapOf("items" to "1"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default",  risk = "allow", warnings = setOf("order field cannot be found")), result)

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

        val ruleEngine = WorkflowEvaluator(workflow)
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

        val ruleEngine = WorkflowEvaluator(workflow)
        Assertions.assertEquals(
            WorkflowResult(workflow = "test", ruleSet = "default", rule = "default", risk = "allow"), ruleEngine.evaluate(
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

        val result = WorkflowEvaluator(workflow).evaluate(mapOf("items" to 1))
        Assertions.assertEquals(WorkflowResult(workflow = "test", risk = "allow",  ruleSet = "default", rule = "default", warnings = setOf("order field cannot be found")), result)
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
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("item" to "item"))
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
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("item" to "item"))
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
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("string" to "item"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `evaluating not in of basic property should work`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a'  string not in 'item' return block
                default allow
            end
        """
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("string" to "auto"))
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
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("order" to mapOf("payment_method" to mapOf("card_bin"  to "item"))))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "item_a", risk = "block"), result)

    }

    @Test
    fun `given a list that not contains a value when evaluating a nested property must return true`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.payment_method.card_bin not in 'item', 'item2', 'item3' return block
                default allow
            end
        """
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("order" to mapOf("payment_method" to mapOf("card_bin"  to "item5"))))
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
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("item2" to "item"))
        Assertions.assertEquals(WorkflowResult(workflow = "test", risk = "allow", ruleSet = "default", rule = "default", warnings = setOf("item field cannot be found")), result)
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
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("string" to "item", "card_bins" to listOf("item1", "item2", "item")))
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a nested property when evaluating must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' string in smart_lists.card_bins return block
                default allow
            end
        """
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("string" to "item", "smart_lists" to mapOf("card_bins" to listOf("item1", "item2", "item"))))
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
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("string" to "item", "card_bin" to listOf("item1", "item2", "item")))
        Assertions.assertEquals(WorkflowResult(workflow = "test", risk = "allow", ruleSet = "default", rule = "default", warnings = setOf("smart_lists field cannot be found")), result)
    }


    @Test
    fun `given an empty stored list when evaluating a list must return allow`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a'  card_bin contains list('card_bins') return block
                default allow
            end
        """
        val result = WorkflowEvaluator(workflow).evaluate(mapOf("string" to "item", "card_bin" to listOf("item1", "item2", "item")))
        Assertions.assertEquals(WorkflowResult(workflow = "test", risk = "allow", ruleSet = "default", rule = "default"), result)
    }

    @Test
    fun `given an existing stored list when evaluating a list must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' card_bin contains list('card_bins') return block
                default allow
            end
        """
        val request = mapOf("card_bin" to "item1", "card_bins" to listOf("item3", "item2", "item"))
        val lists = mapOf("card_bins" to listOf("item1"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given an existing stored list when evaluating a not contains list must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' card_bin not contains list('card_bins') return block
                default allow
            end
        """
        val request = mapOf("card_bin" to "item2")
        val lists = mapOf("card_bins" to listOf("item1"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given an existing stored list when evaluating a not in list must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' card_bin not in list('card_bins') return block
                default allow
            end
        """
        val request = mapOf("card_bin" to "item2")
        val lists = mapOf("card_bins" to listOf("item1"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }


    @Test
    fun `given a composed expression with an existing stored list when evaluating a list must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' card_bin contains list('card_bins') and device_id = 'android' return block
                default allow
            end
        """
        val request = mapOf("card_bin" to "item1", "card_bins" to listOf("item5", "item2", "item"), "device_id" to "android")
        val lists = mapOf("card_bins" to listOf<String>("item1"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a composed expression with an existing stored list when evaluating a list  with a nested property must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' order.payment_method.card_bin contains list('card_bins') and device_id = 'android' return block
                default allow
            end
        """
        val request = mapOf("order" to mapOf("payment_method" to mapOf("card_bin" to "item1")), "card_bins" to listOf("item5", "item2", "item"), "device_id" to "android")
        val lists = mapOf("card_bins" to listOf<String>("item1"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a composed expression with an existing stored list when evaluating a list with a non existent nested property must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' order.payment_method.not_card_bin contains list('card_bins') and device_id = 'android' return block
                default allow
            end
        """
        val request = mapOf("order" to mapOf("payment_method" to mapOf("card_bin" to "item")), "card_bins" to listOf("item5", "item2", "item"), "device_id" to "android")
        val lists = mapOf("card_bins" to listOf<String>("item1"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default", risk = "allow", warnings = setOf("not_card_bin field cannot be found")), result)
    }

    @Test
    fun `given a composed expression with an existing stored list when evaluating not contains a list with a non existent nested property must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' order.payment_method.not_card_bin not contains list('card_bins') and device_id = 'android' return block
                default allow
            end
        """
        val request = mapOf("order" to mapOf("payment_method" to mapOf("card_bin" to "item")), "card_bins" to listOf("item5", "item2", "item"), "device_id" to "android")
        val lists = mapOf("card_bins" to listOf<String>("item1"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default", risk = "allow", warnings = setOf("not_card_bin field cannot be found")), result)
    }

    @Test
    fun `given a composed expression with an existing stored list when evaluating not in a list with a non existent nested property must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' order.payment_method.not_card_bin not in list('card_bins') and device_id = 'android' return block
                default allow
            end
        """
        val request = mapOf("order" to mapOf("payment_method" to mapOf("card_bin" to "item")), "card_bins" to listOf("item5", "item2", "item"), "device_id" to "android")
        val lists = mapOf("card_bins" to listOf("item1"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default", risk = "allow", warnings = setOf("not_card_bin field cannot be found")), result)
    }

    @Test
    fun `given a collection and a existing stored list when evaluating a list must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' payment_methods.any { card_bin in list('card_bins')} return block
                default allow
            end
        """
        val request = mapOf(
            "payment_methods" to listOf(
                mapOf(
                    "card_bin" to "477213"
                )
            )
        )
        val lists = mapOf("card_bins" to listOf("477213"))
        val result = WorkflowEvaluator(workflow).evaluate(request, lists)
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a collection and a literal list when evaluating a list must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' device_id in  'itemA', 'itemB', 'itemC' return block
                default allow
            end
        """
        val request = mapOf(
            "device_id" to "itemC"
        )
        val result = WorkflowEvaluator(workflow).evaluate(request, mapOf())
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a string and a value when using contains operator must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' device_agent contains 'iPhone' return block
                default allow
            end
        """
        val request = mapOf(
            "device_agent" to "Mozilla iPhone bla bla bla "
        )
        val result = WorkflowEvaluator(workflow).evaluate(request, mapOf())
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a string and a literal value when evaluating contains must return block`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' payment_methods.any { card_bin contains '477' } return block
                default allow
            end
        """
        val request = mapOf(
            "payment_methods" to listOf(
                mapOf(
                    "card_bin" to "477213"
                )
            )
        )
        val result = WorkflowEvaluator(workflow).evaluate(request, mapOf())
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "dummy", rule = "rule_a", risk = "block"), result)
    }

    @Test
    fun `given a string and a literal value when evaluating in must return default allow`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' payment_methods.any { card_bin in '477' } return block
                default allow
            end
        """
        val request = mapOf(
            "payment_methods" to listOf(
                mapOf(
                    "card_bin" to "477213"
                )
            )
        )
        val result = WorkflowEvaluator(workflow).evaluate(request, mapOf())
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default", risk = "allow"), result)
    }

    @Test
    fun `given a string and a literal value when evaluating equals must return default allow`() {
        val workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'rule_a' payment_methods.any { card_bin = '477' } return block
                default allow
            end
        """
        val request = mapOf(
            "payment_methods" to listOf(
                mapOf(
                    "card_bin" to "477213"
                )
            )
        )
        val result = WorkflowEvaluator(workflow).evaluate(request, mapOf())
        Assertions.assertEquals(WorkflowResult(workflow = "test", ruleSet = "default", rule = "default", risk = "allow"), result)
    }
}