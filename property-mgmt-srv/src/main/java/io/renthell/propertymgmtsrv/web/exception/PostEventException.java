package io.renthell.propertymgmtsrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PostEventException extends RuntimeException {

    private String errorCode;
    private String message;

    public PostEventException(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }
}
