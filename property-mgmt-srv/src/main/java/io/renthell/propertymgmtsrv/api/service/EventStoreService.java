package io.renthell.propertymgmtsrv.api.service;

import io.renthell.propertymgmtsrv.api.model.PropertyTransactionApi;

public interface EventStoreService {

    public Boolean addPropertyTransaction(PropertyTransactionApi item);
}
