package io.renthell.propertymgmtsrv.persistence.impl;

import io.renthell.propertymgmtsrv.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import io.renthell.propertymgmtsrv.persistence.PropertyRepo;

import java.util.Date;
import java.util.List;

@Repository
public class PropertyRepoMongo implements PropertyRepo {

    private final MongoOperations mongoOperations;

    @Autowired
    public PropertyRepoMongo(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Property findOne(String id) {
        return mongoOperations.findOne(new Query(Criteria.where("identifier").is(id)), Property.class);
    }

    @Override
    public List<Property> findAll() {
        return mongoOperations.findAll(Property.class);
    }

    @Override
    public Property save(Property property) {
        if(property.getId() == null) {
            property.setCreatedDate(new Date());
            property.setUpdated(false);
        }
        else {
            property.setModifiedDate(new Date());
            property.setUpdated(true);
        }
        mongoOperations.save(property);
        return property;
    }
}
