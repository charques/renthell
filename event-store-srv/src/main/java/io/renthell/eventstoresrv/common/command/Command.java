package io.renthell.eventstoresrv.common.command;

/**
 * Created by cfhernandez on 10/7/17.
 */
public abstract class Command {
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}