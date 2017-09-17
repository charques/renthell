package io.renthell.eventstoresrv.web.dto;

import io.renthell.eventstoresrv.web.events.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by cfhernandez on 17/9/17.
 */
@Getter
@Setter
@ToString
public class EventDto {

    private String uuid;

    private Date creationDate;
    private String correlationId;
    private String type;

    private BaseEvent event;
}
