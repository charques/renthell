package io.renthell.eventstoresrv.exceptions;

public class EventStoreException extends Exception {
    public EventStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
