package io.renthell.eventstoresrv.service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRetrievingException extends RuntimeException {

    private Throwable throwable;

    public EventRetrievingException(Throwable e) {
        super();
        this.throwable = e;
    }
}
