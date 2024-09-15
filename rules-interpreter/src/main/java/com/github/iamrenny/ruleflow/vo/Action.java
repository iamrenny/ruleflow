package com.github.iamrenny.ruleflow.vo;

import java.util.Map;

public class Action {
    private String name;
    private Map<String, String> params;

    Action(String name, Map<String, String> params) {
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}