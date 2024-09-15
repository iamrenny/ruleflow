package com.github.iamrenny.ruleflow.vo;

public class WorkflowInfo {
    private final String country;
    private final String version;
    private final String workflowName;

    public WorkflowInfo(String country, String workflowName) {
        this(country, null, workflowName);
    }

    public WorkflowInfo(String country, String version, String workflowName) {
        this.country = country;
        this.version = version;
        this.workflowName = workflowName;
    }

    public String getCountry() {
        return country;
    }

    public String getVersion() {
        return version;
    }

    public String getWorkflowName() {
        return workflowName;
    }
}