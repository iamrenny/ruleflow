package com.rappi.fraud.rules.parser


import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.rappi.fraud.rules.parser.visitors.GrammarVisitor
import com.rappi.fraud.rules.parser.visitors.RulesetVisitor
import com.rappi.fraud.rules.parser.listeners.ErrorListener
import com.rappi.fraud.rules.parser.vo.WorkflowResult
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class Workflow(val workflow: String) {

    private val tree: ANAParser.ParseContext

    init {
        val input = CharStreams.fromString(workflow)
        val lexer = ANALexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = ANAParser(tokens)
        parser.addErrorListener(ErrorListener())
        tree = parser.parse()
    }

    fun evaluate(request: Map<String, Any>, list: Map<String, Set<String>>  = mapOf()): WorkflowResult {
        return RulesetVisitor(request, list)
            .visit(tree)
    }

    fun validateAndGetWorkflowName(): String {
        return GrammarVisitor().visit(tree)
    }
}