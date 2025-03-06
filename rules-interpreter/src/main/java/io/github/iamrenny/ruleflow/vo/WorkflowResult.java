package io.github.iamrenny.ruleflow.vo;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class WorkflowResult {
    private Long id;
    private String workflow;
    private String ruleset;
    private String rule;
    private String decision;
    private Set<String> warnings;
    private Map<String, Map<String, String>> actionsWithParams;
    private Instant timestamp;
    private List<Result> results;
    private WorkflowInfo workflowInfo;
    private boolean error;


    public WorkflowResult(String workflow, String ruleset, String rule, String decision) {
        this.workflow = workflow;
        this.ruleset = ruleset;
        this.rule = rule;
        this.decision = decision;
    }

    public WorkflowResult(String workflow, String ruleset, String rule, String decision, Set<String> warnings) {
        this.workflow = workflow;
        this.ruleset = ruleset;
        this.rule = rule;
        this.decision = decision;
        this.warnings = warnings;
    }

    public WorkflowResult(String workflow, String ruleset, String rule, String decision, List<Result> results) {
        this.workflow = workflow;
        this.ruleset = ruleset;
        this.rule = rule;
        this.decision = decision;
        this.results = results;
    }

    public WorkflowResult(String workflow, String ruleset, String rule, String decision, Set<String> warnings, Map<String, Map<String, String>> actionsWithParams, Instant timestamp, List<Result> results) {
        this.workflow = workflow;
        this.ruleset = ruleset;
        this.rule = rule;
        this.decision = decision;
        this.warnings = warnings;
        this.actionsWithParams = actionsWithParams;
        this.timestamp = timestamp;
        this.results = results;
    }

    public WorkflowResult(String workflow, String ruleset, String rule, String decision, Set<String> warnings, Map<String, Map<String, String>> actionsMap) {
        this.workflow = workflow;
        this.ruleset = ruleset;
        this.rule = rule;
        this.decision = decision;
        this.warnings = warnings;
        this.actionsWithParams = actionsMap;
    }

    public WorkflowResult(String workflow, String ruleset, String rule, String decision, Set<String> warnings, Map<String, Map<String, String>> actionsMap, boolean isError) {
        this.workflow = workflow;
        this.ruleset = ruleset;
        this.rule = rule;
        this.decision = decision;
        this.warnings = warnings;
        this.actionsWithParams = actionsMap;
        this.error = isError;
    }
}