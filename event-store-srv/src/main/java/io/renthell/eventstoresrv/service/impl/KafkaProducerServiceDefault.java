package io.renthell.eventstoresrv.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import io.renthell.eventstoresrv.exceptions.EventStoreException;
import io.renthell.eventstoresrv.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerServiceDefault implements KafkaProducerService {

    @Autowired
    private ObjectMapper jsonMapper;

    @Value("${kafka.topic.events}")
    private String EVENTS_TOPIC;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishEvent(RawEvent rawEvent) throws EventStoreException {

        String eventAsString = null;
        try {
            eventAsString = jsonMapper.writeValueAsString(rawEvent);
        } catch (final JsonProcessingException ex) {
            throw new EventStoreException("Error serializing the event: " + rawEvent.toString(), ex);
        }

        kafkaTemplate.send(EVENTS_TOPIC, eventAsString);
    }

}
