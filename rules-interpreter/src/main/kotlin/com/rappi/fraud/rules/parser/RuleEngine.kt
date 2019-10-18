package com.rappi.fraud.rules.parser

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.RuleSetEvaluator
import com.rappi.fraud.rules.parser.listeners.ErrorListener
import com.rappi.fraud.rules.parser.listeners.WorkflowListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

class RuleEngine(private val workflow: String) {

    private val tree: ANAParser.ParseContext

    init {
        val input = CharStreams.fromString(workflow)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        parser.addErrorListener(ErrorListener())
        tree = parser.parse()
        val walker = ParseTreeWalker()
        val listener = WorkflowListener()
        walker.walk(listener, tree)
    }

    fun evaluate(data: Map<String, Any>): String {
        return RuleSetEvaluator(data).visit(tree)
    }
}