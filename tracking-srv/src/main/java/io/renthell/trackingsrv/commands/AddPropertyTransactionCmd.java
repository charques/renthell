package io.renthell.trackingsrv.commands;

import io.renthell.trackingsrv.common.command.Command;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Getter
@Setter
public class AddPropertyTransactionCmd extends Command {

    @NotNull
    private String identifier;
}
