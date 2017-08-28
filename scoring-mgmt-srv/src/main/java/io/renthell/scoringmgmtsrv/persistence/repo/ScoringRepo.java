package io.renthell.scoringmgmtsrv.persistence.repo;

import io.renthell.scoringmgmtsrv.persistence.model.Scoring;

import java.util.List;

/**
 * Created by cfhernandez on 6/8/17.
 */
public interface ScoringRepo {

    List<Scoring> find(String transactionId, Integer month, Integer year, String postalCode);

    List<Scoring> findAll();

    Scoring save(Scoring rentScoring);

}
