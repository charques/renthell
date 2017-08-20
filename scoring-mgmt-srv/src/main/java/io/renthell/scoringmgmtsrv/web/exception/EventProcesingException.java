package io.renthell.scoringmgmtsrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventProcesingException extends RuntimeException {

    private Throwable throwable;

    public EventProcesingException(Throwable throwable) {
        super();
        this.throwable = throwable;
    }
}
