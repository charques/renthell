package io.renthell.alertmgmtsrv.service.rulesengine.rules;

import lombok.Getter;

@Getter
public class RuleResult {
    private String description;

    public RuleResult(String ruleName, String resultDescription) {
        this.description = ruleName + ": " + resultDescription;
    }

}
