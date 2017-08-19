package io.renthell.eventstoresrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.persistence.model.RawEvent;
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
    public void publishEvent(RawEvent rawEvent) throws JsonProcessingException {

        String eventAsString = null;
        eventAsString = jsonMapper.writeValueAsString(rawEvent);
        kafkaTemplate.send(EVENTS_TOPIC, eventAsString);
    }

}
