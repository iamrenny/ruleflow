import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RuleEvaluatorTest {
    @Test
    fun `given a rule when validating it should return true`() {
        val visitor = RuleEvaluator("x + y = 15", mapOf("x" to 5, "y" to 10), mapOf())
        assertEquals("= true", visitor.evaluate())
    }
    @Test
    fun `given a rule when validating it should return false`() {
        val visitor = RuleEvaluator("x + y = 16 return true", mapOf("x" to 5, "y" to 10), mapOf())
        assertEquals("= false", visitor.evaluate())
    }
    @Test
    fun `given an exception rule when validating it should return error message`() {
        val visitor = RuleEvaluator("as0da9ad +", mapOf("x" to 5, "y" to 10), mapOf())
        assertEquals("error: Error at line 1:10 - mismatched input '<EOF>' expecting {STRING_NOT_SPECIAL_CHARS, '.', CURRENT_DATE, DATE_DIFF, 'abs', REGEX_STRIP, '(', K_NULL, DAY_OF_WEEK, ID, NUMERIC_LITERAL, BOOLEAN_LITERAL, SQUOTA_STRING}\n" +
                "as0da9ad +", visitor.evaluate())
    }
}