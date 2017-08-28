package io.renthell.eventstoresrv.web.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by cfhernandez on 28/8/17.
 */
@Getter
@Setter
@ToString
public class PropertyTransactionConfirmEvent extends BaseEvent {

    private String identifier;

    public PropertyTransactionConfirmEvent() {
        super();
    }

    public PropertyTransactionConfirmEvent(final String correlationId) {
        super(correlationId);
    }

}
