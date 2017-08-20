package io.renthell.eventstoresrv.service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRetrievingException extends RuntimeException {

    private String id;
    private Throwable throwable;

    public EventRetrievingException(String id, Throwable throwable) {
        super();
        this.id = id;
        this.throwable = throwable;
    }
}
