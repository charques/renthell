package io.renthell.propertymgmtsrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BadRequestException extends RuntimeException {

    private Throwable throwable;

    public BadRequestException(Throwable throwable) {
        super();
        this.throwable = throwable;
    }
}