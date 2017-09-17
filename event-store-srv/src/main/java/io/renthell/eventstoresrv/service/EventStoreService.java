package io.renthell.eventstoresrv.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.eventstoresrv.service.exception.EventRetrievingException;
import io.renthell.eventstoresrv.web.dto.EventDto;
import io.renthell.eventstoresrv.web.events.BaseEvent;

public interface EventStoreService {

    String saveAndPublish(final BaseEvent event, final String correlationId) throws JsonProcessingException;

    EventDto findById(final String id) throws EventRetrievingException;
}
