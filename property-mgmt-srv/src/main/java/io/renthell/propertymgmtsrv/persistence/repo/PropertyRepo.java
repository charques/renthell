package io.renthell.propertymgmtsrv.persistence.repo;


import io.renthell.propertymgmtsrv.persistence.model.Property;

import java.util.List;

/**
 * Created by cfhernandez on 10/7/17.
 */
public interface PropertyRepo {

    Property findOne(String id);

    List<Property> findAll();

    Property save(Property property);

}
