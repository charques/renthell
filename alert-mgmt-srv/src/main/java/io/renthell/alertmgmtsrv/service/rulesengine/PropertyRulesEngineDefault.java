package io.renthell.alertmgmtsrv.service.rulesengine;

import io.renthell.alertmgmtsrv.service.rulesengine.rules.OverPriceAverageRule;
import io.renthell.alertmgmtsrv.service.rulesengine.rules.PropertyRule;
import io.renthell.alertmgmtsrv.service.rulesengine.rules.RuleResult;
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
    public List<RuleResult> evaluateRules(PropertyDto propertyDto, List<ScoringStatsDto> scoringStatsDtoList) {
        List<RuleResult> ruleResultList = new ArrayList<>();
        for (PropertyRule rule : rules) {

            for(ScoringStatsDto scoringStatsDto : scoringStatsDtoList) {
                RuleResult ruleResult = rule.evaluate(propertyDto, scoringStatsDto);
                if (ruleResult != null) {
                    ruleResultList.add(ruleResult);
                }
            }
        }
        return ruleResultList;
    }
}
