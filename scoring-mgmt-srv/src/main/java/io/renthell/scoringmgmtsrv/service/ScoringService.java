package io.renthell.scoringmgmtsrv.service;

import io.renthell.scoringmgmtsrv.web.dto.PropertyDto;

import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;

import java.util.List;

public interface ScoringService {

    public ScoringStatsDto addPropertyToScoring(PropertyDto propertyDto);

    public List<ScoringStatsDto> findAll();

}
