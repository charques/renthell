package io.renthell.eventstoresrv.web.events;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Getter
@Setter
public abstract class BaseEvent  implements Serializable {

    protected BaseEvent() {
        super();
    }
}
