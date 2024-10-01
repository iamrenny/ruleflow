package com.github.iamrenny.ruleflow.vo;

import com.github.iamrenny.ruleflow.RuleFlowLanguageParser;
import com.github.iamrenny.ruleflow.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class WorkflowResult {
    private String workflow;
    private String ruleSet;
    private String rule;
    private String result;
    private Set<String> actions;
    private Set<String> warnings;
    private Map<String, Map<String, String>> actionsWithParams;
    private WorkflowInfo workflowInfo;
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
        this.error = error;
    }

    public WorkflowResult(String workflow, String result) {
        this(workflow, null, null, result, null, null, Map.of(), null,null, false);
    }

    public WorkflowResult(String workflow, String ruleset, String rule, String result, Set<String> warnings) {
        this.workflow = workflow;
        this.ruleSet = ruleset;
        this.rule = rule;
        this.result = result;
        this.warnings = warnings;
    }

    public WorkflowResult(String workflow, String ruleset, String rule, String result,  Map<String, Map<String, String>> actionsWithParams) {
        this.workflow = workflow;
        this.ruleSet = ruleset;
        this.rule = rule;
        this.result = result;
        this.actionsWithParams = actionsWithParams;
    }

    public WorkflowResult(String workflow, String ruleset, String rule, String result) {
        this.workflow = workflow;
        this.ruleSet = ruleset;
        this.rule = rule;
        this.result = result;
    }

    public <E, K, V> WorkflowResult(String workflow, String ruleset, String rule, String result, Set<String> warnings, Map<String, Map<String,String>> actionsWithParams) {
        this.workflow = workflow;
        this.ruleSet = ruleset;
        this.rule = rule;
        this.result = result;
        this.warnings = warnings;
        this.actionsWithParams = actionsWithParams;
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
        if(actionsWithParams == null) {
            return null;
        }
            Map<String, Map<String, String>> actionsMap = actionsWithParams.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                    (existing, replacement) -> {
                        existing.putAll(replacement); // merging the two maps
                        return existing;
                    }));
            return actionsMap.keySet();
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

    public boolean isError() {
        return error;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public void setRuleSet(String ruleSet) {
        this.ruleSet = ruleSet;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setWarnings(Set<String> warnings) {
        this.warnings = warnings;
    }

    public void setActionsWithParams(Map<String, Map<String, String>> actionsWithParams) {
        this.actionsWithParams = actionsWithParams;
    }

    public void setWorkflowInfo(WorkflowInfo workflowInfo) {
        this.workflowInfo = workflowInfo;
    }

    public void setError(boolean error) {
        this.error = error;
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
            ", error=" + error +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkflowResult that)) return false;
        return isError() == that.isError() && Objects.equals(getWorkflow(), that.getWorkflow()) && Objects.equals(getRuleSet(), that.getRuleSet()) && Objects.equals(getRule(), that.getRule()) && Objects.equals(getResult(), that.getResult()) && Objects.equals(getActions(), that.getActions()) && Objects.equals(getWarnings(), that.getWarnings()) && Objects.equals(getActionsWithParams(), that.getActionsWithParams()) && Objects.equals(getWorkflowInfo(), that.getWorkflowInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWorkflow(), getRuleSet(), getRule(), getResult(), getActions(), getWarnings(), getActionsWithParams(), getWorkflowInfo(), isError());
    }
}