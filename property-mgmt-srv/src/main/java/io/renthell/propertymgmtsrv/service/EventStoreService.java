package io.renthell.propertymgmtsrv.service;

import io.renthell.propertymgmtsrv.model.Property;

public interface EventStoreService {

    public Boolean addPropertyTransaction(Property item);
}
