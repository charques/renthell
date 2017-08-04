package io.renthell.propertymgmtsrv.eventhandlers.consumer;

import io.renthell.propertymgmtsrv.api.exception.PropertyMgmtException;
import io.renthell.propertymgmtsrv.api.service.PropertyService;
import io.renthell.propertymgmtsrv.configuration.KafkaConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

@Slf4j
public class PropertyAddedEventConsumer implements Runnable {

    private final Consumer<String, String> consumer;
    private final List<String> topics;
    private final int id;
    private PropertyService propertyService;

    public PropertyAddedEventConsumer(int id, KafkaConfiguration kafkaConfiguration, PropertyService propertyService) {
        this.id = id;
        this.topics = Arrays.asList(kafkaConfiguration.getEventsTopic());
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaConfiguration.getBootstrapServers());
        props.put("group.id", kafkaConfiguration.getEventsGroupId());
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(props);
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                for (ConsumerRecord<String, String> record : records) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("partition", record.partition());
                    data.put("offset", record.offset());
                    data.put("value", record.value());
                    log.info("Property event consumed: " + this.id + ": " + data);

                    // TODO: como aseguro que es un property? Por el topic?
                    propertyService.save(record.value());
                }
            }
        } catch (WakeupException e) {
        // ignore for shutdown
        } catch (PropertyMgmtException e) {
            // TODO: review
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        consumer.wakeup();
    }
}