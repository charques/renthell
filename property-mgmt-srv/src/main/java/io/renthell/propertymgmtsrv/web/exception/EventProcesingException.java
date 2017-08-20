package io.renthell.propertymgmtsrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventProcesingException extends RuntimeException {

    private String id;
    private Throwable throwable;

    public EventProcesingException(String id, Throwable throwable) {
        super();
        this.id = id;
        this.throwable = throwable;
    }
}
