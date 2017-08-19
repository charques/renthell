package io.renthell.eventstoresrv.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.eventstoresrv.service.exception.EventRetrievingException;
import io.renthell.eventstoresrv.web.events.BaseEvent;

public interface EventStoreService {

    public String saveAndPublish(final BaseEvent event) throws JsonProcessingException;

    public BaseEvent findById(final String id) throws EventRetrievingException;
}
