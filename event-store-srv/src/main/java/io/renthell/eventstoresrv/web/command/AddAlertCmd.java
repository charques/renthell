package io.renthell.eventstoresrv.web.command;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cfhernandez on 4/9/17.
 */
@Getter
@Setter
@ToString
public class AddAlertCmd extends Command {

    private String alertId;

    private String propertyId;
    private String transactionId;
    private String alertDescriptor;

    private String createdDate;
}
