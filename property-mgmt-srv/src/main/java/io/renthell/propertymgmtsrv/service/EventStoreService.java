package io.renthell.propertymgmtsrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.propertymgmtsrv.web.dto.PropertyInputDto;

public interface EventStoreService {

    public Boolean produceAddPropertyTransactionEvent(PropertyInputDto item) throws JsonProcessingException;

    public Boolean produceConfirmPropertyTransactionEvent(String propertyId, String transactionId);
}
