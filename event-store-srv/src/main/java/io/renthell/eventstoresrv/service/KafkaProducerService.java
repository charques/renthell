package io.renthell.eventstoresrv.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.eventstoresrv.persistence.model.RawEvent;

public interface KafkaProducerService {

    void publishEvent(final RawEvent rawEvent) throws JsonProcessingException;
}
