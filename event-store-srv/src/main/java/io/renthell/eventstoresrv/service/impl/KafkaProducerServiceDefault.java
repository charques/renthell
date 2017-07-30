package io.renthell.eventstoresrv.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import io.renthell.eventstoresrv.configuration.KafkaConfiguration;
import io.renthell.eventstoresrv.exceptions.EventStoreException;
import io.renthell.eventstoresrv.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class KafkaProducerServiceDefault implements KafkaProducerService {

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    @Override
    public void publishEvent(RawEvent rawEvent) throws EventStoreException {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", kafkaConfiguration.getBootstrapServers());
        properties.setProperty("key.serializer", StringSerializer.class.getCanonicalName());
        properties.setProperty("value.serializer", StringSerializer.class.getCanonicalName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String eventAsString = null;
        try {
            eventAsString = jsonMapper.writeValueAsString(rawEvent);
        } catch (final JsonProcessingException ex) {
            throw new EventStoreException("Error serializing the event: " + rawEvent.toString(), ex);
        }

        producer.send(new ProducerRecord<String, String>(kafkaConfiguration.getEventsTopic(), eventAsString));
        producer.close();
    }

}
