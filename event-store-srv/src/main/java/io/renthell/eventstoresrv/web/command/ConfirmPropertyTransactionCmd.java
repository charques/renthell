package io.renthell.eventstoresrv.web.command;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by cfhernandez on 28/8/17.
 */
@Getter
@Setter
@ToString
public class ConfirmPropertyTransactionCmd extends Command {

    @NotNull
    private String identifier;
}
