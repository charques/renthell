package io.renthell.commandhandlersrv.common.persistence.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

/**
 * Created by cfhernandez on 10/7/17.
 */
@Getter
@Setter
@ToString
public class RawEvent  implements Serializable {

    /**
     * Global unique identifier
     */
    @Id
    private String id;

    /**
     * Assigned by database, identical for all events saved in one transaction
     */
    private Instant transactionTime;

    /**
     * This ID is the same for all events committed in the same transaction.
     */
    private String correlationId;

    private String payload;

    private String type;

    public RawEvent() {
        super();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.id = UUID.randomUUID().toString();
        this.transactionTime = timestamp.toInstant();
    }

    public RawEvent(final String correlationId, final String payload, final String type) {
        super();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.id = UUID.randomUUID().toString();
        this.transactionTime = timestamp.toInstant();
        this.correlationId = correlationId;
        this.payload = payload;
        this.type = type;
    }
}
