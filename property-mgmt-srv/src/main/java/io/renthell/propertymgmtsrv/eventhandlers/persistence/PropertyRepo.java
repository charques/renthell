package io.renthell.propertymgmtsrv.eventhandlers.persistence;

import io.renthell.propertymgmtsrv.eventhandlers.model.Property;

/**
 * Created by cfhernandez on 10/7/17.
 */
public interface PropertyRepo {

    Property findOne(String uuid);

    Property save(Property property);

}
