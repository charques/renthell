package io.renthell.trackingsrv.common.events;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Getter
@Setter
public abstract class BaseEvent  implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * This ID is the same for all events committed in the same transaction.
     */
    private String correlationId;

    protected BaseEvent() {
        super();
    }

    protected BaseEvent(final String correlationId) {
        super();

        this.correlationId = correlationId;
    }
}
