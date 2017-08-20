package io.renthell.propertymgmtsrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.propertymgmtsrv.web.dto.PropertyDto;

public interface EventStoreService {

    public Boolean addPropertyTransaction(PropertyDto item) throws JsonProcessingException;
}
