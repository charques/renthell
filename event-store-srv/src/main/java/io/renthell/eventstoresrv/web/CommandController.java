package io.renthell.eventstoresrv.web;

import io.renthell.eventstoresrv.commands.AddPropertyTransactionCmd;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import io.renthell.eventstoresrv.common.util.CustomErrorType;
import io.renthell.eventstoresrv.events.PropertyTransactionAddedEvent;
import io.renthell.eventstoresrv.service.EventStoreService;
import io.renthell.eventstoresrv.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Created by cfhernandez on 10/7/17.
 */
@RestController
@RequestMapping("/commands")
@Slf4j
public class CommandController {

    @Autowired
    private EventStoreService eventStore;

    @Autowired
    private KafkaProducerService kafkaProducer;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/add-property-transaction", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addPropertyTransaction(final @Valid @RequestBody AddPropertyTransactionCmd addPropertyCommand,
                                             UriComponentsBuilder ucBuilder) {
        log.info("Adding property transaction {}", addPropertyCommand);
        PropertyTransactionAddedEvent event = modelMapper.map(addPropertyCommand, PropertyTransactionAddedEvent.class);
        String correlationId = UUID.randomUUID().toString();
        event.setCorrelationId(correlationId);

        RawEvent rawEvent = eventStore.save(event);
        //kafkaProducer.publishEvent(rawEvent);
        //return new ResponseEntity<>(rawEvent, HttpStatus.CREATED);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/commands/get-raw-event/{id}").buildAndExpand(rawEvent.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/get-raw-event/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getRawEvent(@PathVariable("uuid") String uuid) {
        log.info("Fetching RawEvent with uuid {}", uuid);
        RawEvent rawEvent = eventStore.findById(uuid);
        if (rawEvent == null) {
            log.error("RawEvent with uuid {} not found.", uuid);
            return new ResponseEntity(new CustomErrorType("RawEvent with uuid " + uuid
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<RawEvent>(rawEvent, HttpStatus.OK);
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
