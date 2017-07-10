package io.renthell.trackingsrv.web;

import io.renthell.trackingsrv.service.EventStoreService;
import io.renthell.trackingsrv.events.PropertyTransactionAddedEvent;
import io.renthell.trackingsrv.commands.AddPropertyTransactionCmd;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/propertytransaction", method = RequestMethod.POST)
    public void cratePropertyTransaction(final @Valid @RequestBody AddPropertyTransactionCmd addPropertyCommand) {
        String correlationId = UUID.randomUUID().toString();
        final PropertyTransactionAddedEvent event = new PropertyTransactionAddedEvent(correlationId, addPropertyCommand.getIdentifier());

        eventStore.save(event);
    }
}
