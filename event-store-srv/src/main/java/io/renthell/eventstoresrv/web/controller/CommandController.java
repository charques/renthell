package io.renthell.eventstoresrv.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.eventstoresrv.service.exception.EventRetrievingException;
import io.renthell.eventstoresrv.web.command.AddAlertCmd;
import io.renthell.eventstoresrv.web.command.AddPropertyTransactionCmd;
import io.renthell.eventstoresrv.web.command.ConfirmPropertyTransactionCmd;
import io.renthell.eventstoresrv.web.dto.EventDto;
import io.renthell.eventstoresrv.web.events.AddAlertEvent;
import io.renthell.eventstoresrv.web.events.BaseEvent;
import io.renthell.eventstoresrv.web.events.PropertyTransactionAddEvent;
import io.renthell.eventstoresrv.service.EventStoreService;
import io.renthell.eventstoresrv.web.events.PropertyTransactionConfirmEvent;
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
        log.info("Adding property transaction {}", addPropertyCommand.toString());
        PropertyTransactionAddEvent event = modelMapper.map(addPropertyCommand, PropertyTransactionAddEvent.class);
        String correlationId = UUID.randomUUID().toString();

        return produceEvent(event, correlationId, ucBuilder);
    }

    @RequestMapping(value = "/confirm-property-transaction", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> confirmPropertyTransaction(final @Valid @RequestBody ConfirmPropertyTransactionCmd confirmPropertyCommand,
                                             UriComponentsBuilder ucBuilder) {
        log.info("Confirming property transaction {}", confirmPropertyCommand.toString());
        PropertyTransactionConfirmEvent event = modelMapper.map(confirmPropertyCommand, PropertyTransactionConfirmEvent.class);
        String correlationId = UUID.randomUUID().toString();

        return produceEvent(event, correlationId, ucBuilder);
    }

    @RequestMapping(value = "/add-alert", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addAlert(final @Valid @RequestBody AddAlertCmd addAlertCommand,
                                                 UriComponentsBuilder ucBuilder) {
        log.info("Adding alert {}", addAlertCommand.toString());
        AddAlertEvent event = modelMapper.map(addAlertCommand, AddAlertEvent.class);
        String correlationId = UUID.randomUUID().toString();

        return produceEvent(event, correlationId, ucBuilder);
    }

    @RequestMapping(value = "/get-event/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getEvent(@PathVariable("uuid") String uuid) {
        log.info("Fetching Event with uuid {}", uuid);
        try {
            EventDto eventDto = eventStoreService.findById(uuid);
            if (eventDto != null) {
                return new ResponseEntity<>(eventDto, HttpStatus.OK);
            }
        }
        catch (EventRetrievingException e) {
            log.error("Event retrieving exception: {}", e.getId());
            throw e;
        }

        log.error("Event with uuid {} not found.", uuid);
        throw new EventNotFoundException(uuid);
    }

    private ResponseEntity<?> produceEvent(BaseEvent event, String correlationId, UriComponentsBuilder ucBuilder) {
        try {
            String eventId = eventStoreService.saveAndPublish(event, correlationId);
            log.info("Event produced: {}", eventId);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/command/get-event/{id}").buildAndExpand(eventId).toUri());
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);

        } catch (JsonProcessingException e) {
            log.error("Bad request exception: {}", e.getMessage());
            throw new BadRequestException(e);
        }
    }

}
