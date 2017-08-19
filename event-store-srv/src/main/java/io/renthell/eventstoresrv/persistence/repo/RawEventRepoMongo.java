package io.renthell.eventstoresrv.persistence.repo;

import io.renthell.eventstoresrv.persistence.model.RawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RawEventRepoMongo implements RawEventRepo {

    private final MongoOperations mongoOperations;

    @Autowired
    public RawEventRepoMongo(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public RawEvent findOne(String uuid) {
        return mongoOperations.findOne(new Query(Criteria.where("id").is(uuid)), RawEvent.class);
    }

    @Override
    public RawEvent save(RawEvent rawEvent) {
        mongoOperations.save(rawEvent);
        return rawEvent;
    }
}
