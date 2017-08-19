package io.renthell.eventstoresrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.service.exception.EventRetrievingException;
import io.renthell.eventstoresrv.web.events.BaseEvent;
import io.renthell.eventstoresrv.persistence.model.RawEvent;
import io.renthell.eventstoresrv.persistence.repo.RawEventRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    @Autowired
    private KafkaProducerService kafkaProducer;

    @Override
    public String saveAndPublish(final BaseEvent event) throws JsonProcessingException {
        RawEvent rawEventToSave = convert(event);
        RawEvent rawEventSaved = eventRepo.save(rawEventToSave);

        kafkaProducer.publishEvent(rawEventSaved);

        return rawEventSaved.getId();
    }

    @Override
    public BaseEvent findById(String uuid) throws EventRetrievingException {
        RawEvent rawEvent = eventRepo.findOne(uuid);

        if(rawEvent != null) {
            BaseEvent baseEvent = null;
            try {
                Class c = Class.forName(rawEvent.getType());
                baseEvent = (BaseEvent) c.newInstance();
                String payload = rawEvent.getPayload();
                baseEvent = jsonMapper.readValue(payload, baseEvent.getClass());
                baseEvent.setId(rawEvent.getId());
                return baseEvent;

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e) {
                throw new EventRetrievingException(e);
            }
        }
        return null;
    }

    private RawEvent convert(final BaseEvent event) throws JsonProcessingException {
        final RawEvent rawEvent = new RawEvent();
        rawEvent.setType(event.getClass().getCanonicalName());
        rawEvent.setCorrelationId(event.getCorrelationId());

        String eventAsString = null;
        eventAsString = jsonMapper.writeValueAsString(event);
        rawEvent.setPayload(eventAsString);
        log.info(rawEvent.toString());

        return rawEvent;
    }
}
