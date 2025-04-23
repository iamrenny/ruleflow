package io.github.iamrenny.ruleflow;

import io.github.iamrenny.ruleflow.listeners.ErrorListener;
import io.github.iamrenny.ruleflow.visitors.GrammarVisitor;
import io.github.iamrenny.ruleflow.visitors.RulesetVisitor;
import io.github.iamrenny.ruleflow.vo.WorkflowResult;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;

import java.util.Map;
import java.util.Set;

public class Workflow {

    private final io.github.iamrenny.ruleflow.RuleFlowLanguageParser.ParseContext tree;

    public Workflow(String workflow) {
        CharStream input = CharStreams.fromString(workflow);
        io.github.iamrenny.ruleflow.RuleFlowLanguageLexer lexer = new io.github.iamrenny.ruleflow.RuleFlowLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        io.github.iamrenny.ruleflow.RuleFlowLanguageParser parser = new io.github.iamrenny.ruleflow.RuleFlowLanguageParser(tokens);
        parser.addErrorListener(new ErrorListener());
        this.tree = parser.parse();
    }

    public WorkflowResult evaluate(Map<String, Object> request, Map<String, List<?>> list) {
        return new RulesetVisitor(request, list).visit(tree);
    }

    public WorkflowResult evaluate(Map<String, Object> request) {
        return evaluate(request, Map.of());
    }

    public String validateAndGetWorkflowName() {
        return new GrammarVisitor().visit(tree);
    }
}