package io.renthell.eventstoresrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super();
    }
}
