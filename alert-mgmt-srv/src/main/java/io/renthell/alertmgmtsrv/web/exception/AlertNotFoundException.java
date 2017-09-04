package io.renthell.alertmgmtsrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertNotFoundException extends RuntimeException {

    private String id;

    public AlertNotFoundException(String id) {
        super();
        this.id = id;
    }
}
