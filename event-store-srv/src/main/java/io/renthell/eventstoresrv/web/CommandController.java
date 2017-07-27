package io.renthell.eventstoresrv.web;

import io.renthell.eventstoresrv.commands.AddPropertyTransactionCmd;
import io.renthell.eventstoresrv.common.persistence.event.RawEvent;
import io.renthell.eventstoresrv.events.PropertyTransactionAddedEvent;
import io.renthell.eventstoresrv.service.EventStoreService;
import io.renthell.eventstoresrv.service.KafkaProducerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Created by cfhernandez on 10/7/17.
 */
@RestController
@RequestMapping("/commands")
public class CommandController {

    @Autowired
    private EventStoreService eventStore;

    @Autowired
    private KafkaProducerService kafkaProducer;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/add-property-transaction", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<RawEvent> cratePropertyTransaction(final @Valid @RequestBody AddPropertyTransactionCmd addPropertyCommand) {
        PropertyTransactionAddedEvent event = modelMapper.map(addPropertyCommand, PropertyTransactionAddedEvent.class);
        String correlationId = UUID.randomUUID().toString();
        event.setCorrelationId(correlationId);

        RawEvent rawEvent = eventStore.save(event);
        //kafkaProducer.publishEvent(rawEvent);
        return new ResponseEntity<>(rawEvent, HttpStatus.CREATED);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
