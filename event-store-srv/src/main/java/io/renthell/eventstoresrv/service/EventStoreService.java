package io.renthell.eventstoresrv.service;


import io.renthell.eventstoresrv.common.events.BaseEvent;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;

public interface EventStoreService {

    public RawEvent save(final BaseEvent event);
}
