package io.renthell.propertymgmtsrv.web.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyNotFoundException extends RuntimeException {

    private String id;

    public PropertyNotFoundException(String id) {
        super();
        this.id = id;
    }
}
