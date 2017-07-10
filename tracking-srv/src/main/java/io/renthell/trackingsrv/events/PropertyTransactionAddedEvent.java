package io.renthell.trackingsrv.events;

import io.renthell.trackingsrv.common.events.BaseEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Getter
@Setter
public class PropertyTransactionAddedEvent extends BaseEvent {

    private String identifier;

    public PropertyTransactionAddedEvent() {
        super();
    }

    public PropertyTransactionAddedEvent(final String correlationId, final String identifier) {
        super(correlationId);

        this.identifier = identifier;
    }

}
