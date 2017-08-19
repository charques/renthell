package io.renthell.eventstoresrv.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Document(collection = "rawevent")
@Getter
@Setter
@ToString
public class RawEvent  implements Serializable {

    @Id
    private String id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date creationDate;

    /**
     * This ID is the same for all events committed in the same transaction.
     */
    private String correlationId;
    private String payload;
    private String type;

    public RawEvent() {
        super();

        this.id = UUID.randomUUID().toString();
        this.creationDate = new Date();
    }

    public RawEvent(final String correlationId, final String payload, final String type) {
        super();

        this.id = UUID.randomUUID().toString();
        this.creationDate = new Date();
        this.correlationId = correlationId;
        this.payload = payload;
        this.type = type;
    }
}
