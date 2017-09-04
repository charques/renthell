package io.renthell.alertmgmtsrv.service.rulesengine;

import io.renthell.alertmgmtsrv.service.rulesengine.rules.OverPriceAverageRule;
import io.renthell.alertmgmtsrv.service.rulesengine.rules.PropertyRule;
import io.renthell.alertmgmtsrv.service.rulesengine.rules.UnderPriceAverageRule;
import io.renthell.alertmgmtsrv.web.dto.PropertyDto;
import io.renthell.alertmgmtsrv.web.dto.ScoringStatsDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PropertyRulesEngineDefault implements PropertyRulesEngine {

    private List<PropertyRule> rules = new ArrayList<>();

    public PropertyRulesEngineDefault() {
        rules.add(new UnderPriceAverageRule(15));
        rules.add(new OverPriceAverageRule(15));
    }

    @Override
    public String evaluateRules(PropertyDto propertyDto, ScoringStatsDto scoringStatsDto) {
        Boolean ruleResult = null;
        for (PropertyRule rule : rules) {

            ruleResult = rule.evaluate(propertyDto, scoringStatsDto);
            if(ruleResult) {
                return rule.identifier();
            }
        }
        return null;
    }
}
