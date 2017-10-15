package io.renthell.propertymgmtsrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.propertymgmtsrv.web.dto.PropertyInputDto;

public interface EventStoreService {

    Boolean produceAddPropertyTransactionEvent(PropertyInputDto item) throws JsonProcessingException;

    Boolean produceConfirmPropertyTransactionEvent(String propertyId, String transactionId);
}
