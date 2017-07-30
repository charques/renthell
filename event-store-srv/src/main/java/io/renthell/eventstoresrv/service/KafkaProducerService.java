package io.renthell.eventstoresrv.service;


import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import io.renthell.eventstoresrv.exceptions.EventStoreException;

public interface KafkaProducerService {

    public void publishEvent(final RawEvent rawEvent) throws EventStoreException;
}
