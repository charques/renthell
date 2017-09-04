package io.renthell.alertmgmtsrv.persistence.repo;

import io.renthell.alertmgmtsrv.persistence.model.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class AlertRepoMongo implements AlertRepo {

    private final MongoOperations mongoOperations;

    @Autowired
    public AlertRepoMongo(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Alert findOne(String id) {
        return mongoOperations.findOne(new Query(Criteria.where("id").is(id)), Alert.class);
    }

    @Override
    public List<Alert> findAll() {
        return mongoOperations.findAll(Alert.class);
    }


    @Override
    public Alert save(Alert alert) {
        if(alert.getId() == null) {
            alert.setCreatedDate(new Date());
        }
        mongoOperations.save(alert);
        return alert;
    }
}
