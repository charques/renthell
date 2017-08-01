package io.renthell.propertymgmtsrv.eventhandlers.persistence.impl;

import io.renthell.propertymgmtsrv.eventhandlers.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import io.renthell.propertymgmtsrv.eventhandlers.persistence.PropertyRepo;

@Repository
public class PropertyRepoMongo implements PropertyRepo {

    private final MongoOperations mongoOperations;

    @Autowired
    public PropertyRepoMongo(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Property findOne(String uuid) {
        return mongoOperations.findOne(new Query(Criteria.where("id").is(uuid)), Property.class);
    }

    @Override
    public Property save(Property property) {
        mongoOperations.save(property);
        return property;
    }
}
