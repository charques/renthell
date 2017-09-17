package io.renthell.alertmgmtsrv.service.rulesengine;

import io.renthell.alertmgmtsrv.service.rulesengine.rules.RuleResult;
import io.renthell.alertmgmtsrv.web.dto.PropertyDto;
import io.renthell.alertmgmtsrv.web.dto.ScoringStatsDto;

import java.util.List;

public interface PropertyRulesEngine {

    List<RuleResult> evaluateRules(PropertyDto propertyDto, List<ScoringStatsDto> scoringStatsDto);
}
