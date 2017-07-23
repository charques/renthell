package io.renthell.commandhandlersrv.web;

import io.renthell.commandhandlersrv.commands.AddPropertyTransactionCmd;
import io.renthell.commandhandlersrv.common.persistence.event.RawEvent;
import io.renthell.commandhandlersrv.events.PropertyTransactionAddedEvent;
import io.renthell.commandhandlersrv.service.EventStoreService;
import io.renthell.commandhandlersrv.service.KafkaProducerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public void cratePropertyTransaction(final @Valid @RequestBody AddPropertyTransactionCmd addPropertyCommand) {
        PropertyTransactionAddedEvent event = modelMapper.map(addPropertyCommand, PropertyTransactionAddedEvent.class);
        String correlationId = UUID.randomUUID().toString();
        event.setCorrelationId(correlationId);

        RawEvent rawEvent = eventStore.save(event);
        kafkaProducer.publishEvent(rawEvent);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
