package io.renthell.scoringmgmtsrv.service;

import io.renthell.scoringmgmtsrv.exception.ScoringMgmtException;
import io.renthell.scoringmgmtsrv.model.Property;
import io.renthell.scoringmgmtsrv.model.Scoring;

import java.util.List;

public interface ScoringService {

    public Scoring addProperty(Property property) throws ScoringMgmtException;

    public List<Scoring> findAll();

    //public Scoring findOne(String uuid);
}
