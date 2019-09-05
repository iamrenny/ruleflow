import com.rappi.fraud.analang.ANABaseListener
import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import javafx.util.converter.BigIntegerStringConverter
import jdk.nashorn.internal.ir.annotations.Ignore
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigInteger

class ParseTest {
    val target = EvaluateRulesetVisitor()

    @Test
    fun testParse(){
        val file =  javaClass.classLoader.getResourceAsStream("samples/test.ANA")
        val input = CharStreams.fromStream(file)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        val tree = parser.parse()
        val walker = ParseTreeWalker()
        val listener = ANABaseListener()
        walker.walk(listener, tree)
        Assertions.assertEquals("looks_ok", target.visit(tree))
    }

    @Test
    fun testParse2(){
        val file =  javaClass.classLoader.getResourceAsStream("samples/test2.ANA")
        val input = CharStreams.fromStream(file)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        val tree = parser.parse()
        print(target.visit(tree))
        Assertions.assertEquals("looks_ok", target.visit(tree))
    }


    @Test
    fun testParse3(){
        val file =  javaClass.classLoader.getResourceAsStream("samples/test3.ANA")
        val input = CharStreams.fromStream(file)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        val tree = parser.parse()
        print(target.visit(tree))
    }
}