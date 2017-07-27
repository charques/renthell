package io.renthell.eventstoresrv.service;


import io.renthell.eventstoresrv.common.persistence.event.RawEvent;

public interface KafkaProducerService {

    public void publishEvent(final RawEvent rawEvent);
}
