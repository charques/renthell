package io.renthell.propertymgmtsrv.api.service;

import io.renthell.propertymgmtsrv.api.dto.PropertyTransactionDto;
import io.renthell.propertymgmtsrv.api.exception.PropertyMgmtException;

public interface EventStoreService {

    public Boolean addPropertyTransaction(PropertyTransactionDto item) throws PropertyMgmtException;
}
