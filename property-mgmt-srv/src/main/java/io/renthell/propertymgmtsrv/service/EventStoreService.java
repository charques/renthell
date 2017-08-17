package io.renthell.propertymgmtsrv.service;

import io.renthell.propertymgmtsrv.web.dto.PropertyDto;
import io.renthell.propertymgmtsrv.web.exception.PropertyMgmtException;

public interface EventStoreService {

    public Boolean addPropertyTransaction(PropertyDto item) throws PropertyMgmtException;
}
