package io.renthell.eventstoresrv.service;


import io.renthell.eventstoresrv.common.events.BaseEvent;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import io.renthell.eventstoresrv.exceptions.EventStoreException;

public interface EventStoreService {

    public RawEvent save(final BaseEvent event) throws EventStoreException;

    public RawEvent findById(final String id);
}
