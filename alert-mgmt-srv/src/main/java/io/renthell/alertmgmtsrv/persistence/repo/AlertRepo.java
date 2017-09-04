package io.renthell.alertmgmtsrv.persistence.repo;

import io.renthell.alertmgmtsrv.persistence.model.Alert;

import java.util.List;

/**
 * Created by cfhernandez on 28/8/17.
 */
public interface AlertRepo {

    Alert findOne(String id);

    List<Alert> findAll();

    Alert save(Alert alert);

}
