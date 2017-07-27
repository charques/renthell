package io.renthell.eventstoresrv.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import io.renthell.eventstoresrv.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Slf4j
@Service
public class KafkaProducerServiceDefault implements KafkaProducerService {
    final static String TOPIC = "test";

    private ObjectMapper mapper;

    @Override
    public void publishEvent(RawEvent rawEvent) {
        // Set properties used to configure the producer
        Properties properties = new Properties();
        // Set the brokers (bootstrap servers)
        properties.setProperty("bootstrap.servers", "192.168.99.100:9092");
        // Set how to serialize key/value pairs
        properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String eventAsString = null;
        try {
            eventAsString = mapper.writeValueAsString(rawEvent);
        } catch (final JsonProcessingException ex) {
            throw new IllegalStateException("Error serializing the event: " + rawEvent.toString(), ex);
        }

        producer.send(new ProducerRecord<String, String>(TOPIC, eventAsString));
        producer.close();
    }

    @PostConstruct
    void instantiate() {
        this.mapper = new ObjectMapper();
    }
}
