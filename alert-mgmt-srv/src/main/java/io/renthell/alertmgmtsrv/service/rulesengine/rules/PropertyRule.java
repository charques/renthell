package io.renthell.alertmgmtsrv.service.rulesengine.rules;

import io.renthell.alertmgmtsrv.web.dto.PropertyDto;
import io.renthell.alertmgmtsrv.web.dto.ScoringStatsDto;

public interface PropertyRule {

    RuleResult evaluate(PropertyDto propertyDto, ScoringStatsDto scoringStatsDto);
}
