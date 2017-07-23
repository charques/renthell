package io.renthell.trackingsrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.trackingsrv.persistence.RawEventRepo;
import io.renthell.trackingsrv.common.events.BaseEvent;
import io.renthell.trackingsrv.common.persistence.event.RawEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Service
@Slf4j
public class EventStoreService {
    private ObjectMapper mapper;

    @Autowired
    private RawEventRepo eventRepo;

    public RawEvent save(final BaseEvent event) {
        return eventRepo.save(convert(event));
    }

    private RawEvent convert(final BaseEvent event) {
        final RawEvent rawEvent = new RawEvent();
        rawEvent.setType(event.getClass().getCanonicalName());
        rawEvent.setCorrelationId(event.getCorrelationId());

        String eventAsString = null;
        try {
            eventAsString = mapper.writeValueAsString(event);
        } catch (final JsonProcessingException ex) {
            throw new IllegalStateException("Error serializing the event: " + event.toString(), ex);
        }
        rawEvent.setPayload(eventAsString);

        log.info(rawEvent.toString());

        return rawEvent;
    }

    @PostConstruct
    void instantiate() {
        this.mapper = new ObjectMapper();
    }
}