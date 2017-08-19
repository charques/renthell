package io.renthell.eventstoresrv.web.events;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Getter
@Setter
public abstract class BaseEvent  implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * Unique id.
     */
    private String id;

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
