package io.renthell.propertymgmtsrv.api.service;

import io.renthell.propertymgmtsrv.api.exception.PropertyMgmtException;
import io.renthell.propertymgmtsrv.api.model.Property;

import java.util.List;

public interface PropertyService {

    public Property save(String propertyRawEventString) throws PropertyMgmtException;

    public List<Property> findAll();

    public Property findOne(String uuid);
}
