package com.github.iamrenny.ruleflow.vo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorkflowResult {
    private String workflow;
    private String ruleSet;
    private String rule;
    private String result;
    private Set<String> actions;
    private Set<String> warnings;
    private Map<String, Map<String, String>> actionsWithParams;
    private WorkflowInfo workflowInfo;
    private List<Action> actionsList;
    private boolean error;

    public WorkflowResult(String workflow, String ruleSet, String rule, String result, Set<String> actions, Set<String> warnings, Map<String, Map<String, String>> actionsWithParams, WorkflowInfo workflowInfo, List<Action> actionsList, boolean error) {
        this.workflow = workflow;
        this.ruleSet = ruleSet;
        this.rule = rule;
        this.result = result;
        this.actions = actions;
        this.warnings = warnings;
        this.actionsWithParams = actionsWithParams;
        this.workflowInfo = workflowInfo;
        this.actionsList = actionsList;
        this.error = error;
    }

    public WorkflowResult(String workflow, String result) {
        this(workflow, null, null, result, Set.of(), Set.of(), Map.of(), null, List.of(), false);
    }

    public String getWorkflow() {
        return workflow;
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public String getRule() {
        return rule;
    }

    public String getResult() {
        return result;
    }

    public Set<String> getActions() {
        return actions;
    }

    public Set<String> getWarnings() {
        return warnings;
    }

    public Map<String, Map<String, String>> getActionsWithParams() {
        return actionsWithParams;
    }

    public WorkflowInfo getWorkflowInfo() {
        return workflowInfo;
    }

    public List<Action> getActionsList() {
        return actionsList;
    }

    public boolean isError() {
        return error;
    }

    @Override
    public String toString() {
        return "WorkflowResult{" +
            "workflow='" + workflow + '\'' +
            ", ruleSet='" + ruleSet + '\'' +
            ", rule='" + rule + '\'' +
            ", result='" + result + '\'' +
            ", actions=" + actions +
            ", warnings=" + warnings +
            ", actionsWithParams=" + actionsWithParams +
            ", workflowInfo=" + workflowInfo +
            ", actionsList=" + actionsList +
            ", error=" + error +
            '}';
    }
}