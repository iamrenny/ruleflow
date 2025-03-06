package io.github.iamrenny.ruleflow.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Result {
    private String rule;
    private String ruleset;
    private String decision;
    private List<Action> actions;
    private boolean isShadowed;
}
