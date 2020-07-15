package com.rappi.fraud.rules.parser

import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.evaluators.GrammarEvaluator
import com.rappi.fraud.rules.parser.evaluators.RuleSetEvaluator
import com.rappi.fraud.rules.parser.listeners.ErrorListener
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class WorkflowEvaluator(val workflow: String) {

    private val tree: ANAParser.ParseContext

    init {
        val input = CharStreams.fromString(workflow)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        parser.addErrorListener(ErrorListener())
        tree = parser.parse()
    }

    fun evaluate(request: Map<String, Any>, list: Map<String, List<String>>  = mapOf()): WorkflowResult {
        return RuleSetEvaluator(request, list)
            .visit(tree)
    }

    fun validateAndGetWorkflowName(): String {
        return GrammarEvaluator().visit(tree)
    }
}