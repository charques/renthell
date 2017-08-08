package io.renthell.scoringmgmtsrv.service;

import io.renthell.scoringmgmtsrv.model.Property;
import io.renthell.scoringmgmtsrv.model.Scoring;
import io.renthell.scoringmgmtsrv.model.ScoringStats;

import java.util.List;

public interface ScoringService {

    public Scoring addPropertyToScoring(Property property);

    public List<ScoringStats> findAll();

}
