import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

class RuleEvaluatorTest {

    @Test
    void givenRuleWhenValidatingShouldReturnTrue() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("x", 5);
        variables.put("y", 10);

        RuleEvaluator visitor = new RuleEvaluator("x + y = 15", variables, new HashMap<>());
        assertEquals("true", visitor.evaluate());
    }

    @Test
    void givenRuleWhenValidatingShouldReturnFalse() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("x", 5);
        variables.put("y", 10);

        RuleEvaluator visitor = new RuleEvaluator("x + y = 16 return true", variables, new HashMap<>());
        assertEquals("false", visitor.evaluate());
    }

    @Test
    void givenExceptionRuleWhenValidatingShouldReturnErrorMessage() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("x", 5);
        variables.put("y", 10);

        RuleEvaluator visitor = new RuleEvaluator("as0da9ad +", variables, new HashMap<>());
        assertTrue(visitor.evaluate().contains("error"));
    }
}