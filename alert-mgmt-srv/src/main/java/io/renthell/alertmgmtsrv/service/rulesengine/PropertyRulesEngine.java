package io.renthell.alertmgmtsrv.service.rulesengine;

import io.renthell.alertmgmtsrv.web.dto.PropertyDto;
import io.renthell.alertmgmtsrv.web.dto.ScoringStatsDto;

public interface PropertyRulesEngine {

    String evaluateRules(PropertyDto propertyDto, ScoringStatsDto scoringStatsDto);
}
