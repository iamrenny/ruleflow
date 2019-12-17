package com.rappi.fraud.rules.parser

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.GrammarEvaluator
import com.rappi.fraud.rules.parser.evaluators.RuleSetEvaluator
import com.rappi.fraud.rules.parser.listeners.ErrorListener
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class RuleEngine(val workflow: String) {

    private val tree: ANAParser.ParseContext

    init {
        val input = CharStreams.fromString(workflow)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        parser.addErrorListener(ErrorListener())
        tree = parser.parse()
    }

    fun evaluate(data: Map<String, Any>): WorkflowResult {
        return RuleSetEvaluator(data).visit(tree)
    }

    fun validateAndGetWorkflowName(): String {
        return GrammarEvaluator().visit(tree)
    }
}