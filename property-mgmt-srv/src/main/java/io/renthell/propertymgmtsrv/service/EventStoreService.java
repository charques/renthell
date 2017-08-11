package io.renthell.propertymgmtsrv.service;

import io.renthell.propertymgmtsrv.web.dto.PropertyTransactionDto;
import io.renthell.propertymgmtsrv.exception.PropertyMgmtException;

public interface EventStoreService {

    public Boolean addPropertyTransaction(PropertyTransactionDto item) throws PropertyMgmtException;
}
