package io.renthell.scoringmgmtsrv.persistence.repo;

import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ScoringRepoMongo implements ScoringRepo {

    private final MongoOperations mongoOperations;

    @Autowired
    public ScoringRepoMongo(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public List<Scoring> find(String transactionId, Integer month, Integer year, String postalCode, Integer rooms) {
        Query query = new Query();
        Criteria criteria = Criteria.where("transactionId").is(transactionId);
        if(month != null) {
            criteria.and("month").is(month);
        }
        if(year != null) {
            criteria.and("year").is(year);
        }
        if(postalCode != null) {
            criteria.and("postalCode").is(postalCode);
        }
        if(rooms != null) {
            criteria.and("rooms").is(rooms);
        }
        query.addCriteria(criteria);

        return mongoOperations.find(query, Scoring.class);
    }

    @Override
    public List<Scoring> findAll() {
        return mongoOperations.findAll(Scoring.class);
    }

    @Override
    public Scoring save(Scoring scoring) {
        if(scoring.getId() == null) {
            scoring.setCreatedDate(new Date());
        }
        else {
            scoring.setModifiedDate(new Date());
        }
        mongoOperations.save(scoring);
        return scoring;
    }
}
