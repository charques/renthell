package io.renthell.eventstoresrv.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.eventstoresrv.service.exception.EventRetrievingException;
import io.renthell.eventstoresrv.web.command.AddPropertyTransactionCmd;
import io.renthell.eventstoresrv.web.events.BaseEvent;
import io.renthell.eventstoresrv.web.events.PropertyTransactionAddedEvent;
import io.renthell.eventstoresrv.service.EventStoreService;
import io.renthell.eventstoresrv.web.exception.BadRequestException;
import io.renthell.eventstoresrv.web.exception.EventNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private EventStoreService eventStoreService;

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

        try {
            String eventId = eventStoreService.saveAndPublish(event);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/command/get-event/{id}").buildAndExpand(eventId).toUri());
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);

        } catch (JsonProcessingException e) {
            log.error("Bad request exception: {}", e.getMessage());
            throw new BadRequestException(e);
        }
    }

    @RequestMapping(value = "/get-event/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable("uuid") String uuid) {
        log.info("Fetching Event with uuid {}", uuid);
        try {
            BaseEvent baseEvent = eventStoreService.findById(uuid);
            if (baseEvent != null) {
                return new ResponseEntity<>(baseEvent, HttpStatus.OK);
            }
        }
        catch (EventRetrievingException e) {
            log.error("Event retrieving exception: {}", e.getId());
            throw e;
        }

        log.error("Event with uuid {} not found.", uuid);
        throw new EventNotFoundException(uuid);
    }

}
