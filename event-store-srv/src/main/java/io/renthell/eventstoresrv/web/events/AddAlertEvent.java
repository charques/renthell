package io.renthell.eventstoresrv.web.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cfhernandez on 28/8/17.
 */
@Getter
@Setter
@ToString
public class AddAlertEvent extends BaseEvent {

    private String alertId;

    private String propertyId;
    private String transactionId;
    private String alertDescriptor;

    private String createdDate;

    public AddAlertEvent() {
        super();
    }

    public AddAlertEvent(final String correlationId) {
        super(correlationId);
    }

}
