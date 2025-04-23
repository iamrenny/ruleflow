import io.github.iamrenny.ruleflow.Workflow;
import io.github.iamrenny.ruleflow.vo.WorkflowResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

class ListTest {

    @Test
    public void testListAny() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """;

        Workflow ruleEngine = new Workflow(workflow);
        WorkflowResult expectedResult = new WorkflowResult("test", "dummy", "item_a", "block");
        WorkflowResult result = ruleEngine.evaluate(Map.of(
            "order", Map.of(
                "items", List.of(
                    Map.of("type", "c"),
                    Map.of("type", "b"),
                    Map.of("type", "a")
                )
            )
        ));

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testListAnyMissing() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """;

        Workflow ruleEngine = new Workflow(workflow);
        WorkflowResult expectedResult = new WorkflowResult("test", "default", "default", "allow");
        WorkflowResult result = ruleEngine.evaluate(Map.of(
            "order", Map.of(
                "items", List.of(
                    Map.of("type", "c"),
                    Map.of("type", "b"),
                    Map.of("type", "e")
                )
            )
        ));

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testListAnyNotCollection() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.any { type = 'a' } return block
                default allow
            end
        """;

        WorkflowResult expectedResult = new WorkflowResult("test", "default", "default", "allow", Set.of("order field cannot be found"));
        WorkflowResult result = new Workflow(workflow).evaluate(Map.of("items", "1"));

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testListAll() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' items.all { type = 'a' } return block
                default allow
            end
        """;

        Workflow ruleEngine = new Workflow(workflow);
        WorkflowResult expectedResult = new WorkflowResult("test", "dummy", "item_a", "block");
        WorkflowResult result = ruleEngine.evaluate(Map.of(
            "items", List.of(
                Map.of("type", "a"),
                Map.of("type", "a"),
                Map.of("type", "a")
            )
        ));

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testListAllMissing() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' items.all { type = 'a' } return allow
                default allow
            end
        """;

        Workflow ruleEngine = new Workflow(workflow);
        WorkflowResult expectedResult = new WorkflowResult("test", "default", "default", "allow");
        WorkflowResult result = ruleEngine.evaluate(Map.of(
            "items", List.of(
                Map.of("type", "b"),
                Map.of("type", "a"),
                Map.of("type", "a")
            )
        ));

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testListAllNotCollection() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' order.items.all { type = 'a' } return block
                default allow
            end
        """;

        WorkflowResult expectedResult = new WorkflowResult("test", "default", "default", "allow", Set.of("order field cannot be found"));
        WorkflowResult result = new Workflow(workflow).evaluate(Map.of("items", 1));

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void testListNone() {
        String workflow = """
            workflow 'test'
                ruleset 'dummy'
                    'item_a' items.none { type = '4' } return block
                default allow
            end
        """;

        Workflow ruleEngine = new Workflow(workflow);
        WorkflowResult expectedResult = new WorkflowResult("test", "dummy", "item_a", "block");
        WorkflowResult result = ruleEngine.evaluate(Map.of(
            "items", List.of(
                Map.of("type", "1"),
                Map.of("type", "2"),
                Map.of("type", "3")
            )
        ));

        Assertions.assertEquals(expectedResult, result);
    }
}