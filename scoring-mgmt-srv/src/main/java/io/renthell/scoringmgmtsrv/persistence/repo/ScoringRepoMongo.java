package io.renthell.scoringmgmtsrv.persistence.repo;

import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import io.renthell.scoringmgmtsrv.persistence.repo.ScoringRepo;
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

    @Override
    public Scoring findOne(String transactionId, int month, int year, String postalCode,
                           String district, String city, Integer rooms) {
        Query query = new Query();
        query.addCriteria(Criteria.where("transactionId").is(transactionId)
                .and("month").is(month)
                .and("year").is(year)
                .and("postalCode").is(postalCode)
                .and("district").is(district)
                .and("city").is(city)
                .and("rooms").is(rooms));

        List<Scoring> list = mongoOperations.findAll(Scoring.class);

        return mongoOperations.findOne(query, Scoring.class);
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