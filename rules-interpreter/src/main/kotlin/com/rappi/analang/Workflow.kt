package com.github.iamrenny.rulesflow


import com.rappi.fraud.analang.ANALexer
import com.rappi.fraud.analang.ANAParser
import com.github.iamrenny.rulesflow.visitors.GrammarVisitor
import com.github.iamrenny.rulesflow.visitors.RulesetVisitor
import com.github.iamrenny.rulesflow.listeners.ErrorListener
import com.github.iamrenny.rulesflow.vo.WorkflowResult
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