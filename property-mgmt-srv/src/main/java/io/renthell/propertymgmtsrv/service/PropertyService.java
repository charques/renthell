package io.renthell.propertymgmtsrv.service;

import io.renthell.propertymgmtsrv.exception.PropertyMgmtException;
import io.renthell.propertymgmtsrv.model.Property;

import java.util.List;

public interface PropertyService {

    public Property save(Property propertyToSave) throws PropertyMgmtException;

    public List<Property> findAll();

    public Property findOne(String uuid);
}
