package io.renthell.propertymgmtsrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.propertymgmtsrv.web.dto.PropertyInputDto;

public interface EventStoreService {

    public Boolean addPropertyTransaction(PropertyInputDto item) throws JsonProcessingException;
}
