package io.renthell.eventstoresrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventNotFoundException extends RuntimeException {

    private String id;

    public EventNotFoundException(String id) {
        super();
        this.id = id;
    }
}
