package io.renthell.scoringmgmtsrv.persistence;

import io.renthell.scoringmgmtsrv.model.Scoring;

import java.util.List;

/**
 * Created by cfhernandez on 6/8/17.
 */
public interface ScoringRepo {

    Scoring findOne(String transactionId, int month, int year, String postalCode,
                    String district, String city, Integer rooms);

    List<Scoring> findAll();

    Scoring save(Scoring rentScoring);

}
