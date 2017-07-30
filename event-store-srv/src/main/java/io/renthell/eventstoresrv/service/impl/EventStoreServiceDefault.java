package io.renthell.eventstoresrv.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.renthell.eventstoresrv.common.events.BaseEvent;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import io.renthell.eventstoresrv.exceptions.EventStoreException;
import io.renthell.eventstoresrv.persistence.RawEventRepo;
import io.renthell.eventstoresrv.service.EventStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Service
@Slf4j
public class EventStoreServiceDefault implements EventStoreService {

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private RawEventRepo eventRepo;

    @Override
    public RawEvent save(final BaseEvent event) throws EventStoreException {
        return eventRepo.save(convert(event));
    }

    @Override
    public RawEvent findById(String uuid) {
        return eventRepo.findOne(uuid);
    }

    private RawEvent convert(final BaseEvent event) throws EventStoreException {
        final RawEvent rawEvent = new RawEvent();
        rawEvent.setType(event.getClass().getCanonicalName());
        rawEvent.setCorrelationId(event.getCorrelationId());

        String eventAsString = null;
        try {
            eventAsString = jsonMapper.writeValueAsString(event);
        } catch (final JsonProcessingException ex) {
            throw new EventStoreException("Error serializing the event: " + event.toString(), ex);
        }
        rawEvent.setPayload(eventAsString);
        log.info(rawEvent.toString());

        return rawEvent;
    }

}