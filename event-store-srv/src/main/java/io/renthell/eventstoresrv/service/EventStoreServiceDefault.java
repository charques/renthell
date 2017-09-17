package io.renthell.eventstoresrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.service.exception.EventRetrievingException;
import io.renthell.eventstoresrv.web.dto.EventDto;
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
    public String saveAndPublish(final BaseEvent event, final String correlationId) throws JsonProcessingException {
        String eventAsString = jsonMapper.writeValueAsString(event);
        RawEvent rawEventToSave = new RawEvent(correlationId, eventAsString, event.getClass().getCanonicalName());

        RawEvent rawEventSaved = eventRepo.save(rawEventToSave);

        kafkaProducer.publishEvent(rawEventSaved);

        return rawEventSaved.getId();
    }

    @Override
    public EventDto findById(String uuid) throws EventRetrievingException {
        RawEvent rawEvent = eventRepo.findOne(uuid);

        if(rawEvent != null) {
            try {
                Class c = Class.forName(rawEvent.getType());
                BaseEvent baseEvent = (BaseEvent) c.newInstance();
                String payload = rawEvent.getPayload();
                baseEvent = jsonMapper.readValue(payload, baseEvent.getClass());

                EventDto eventDto = new EventDto();
                eventDto.setUuid(rawEvent.getId());
                eventDto.setType(rawEvent.getType());
                eventDto.setCorrelationId(rawEvent.getCorrelationId());
                eventDto.setCreationDate(rawEvent.getCreationDate());
                eventDto.setEvent(baseEvent);
                return eventDto;

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e) {
                throw new EventRetrievingException(uuid, e);
            }
        }
        return null;
    }

}
