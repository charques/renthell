package io.renthell.trackingsrv.persistence;

import io.renthell.trackingsrv.common.persistence.event.RawEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by cfhernandez on 10/7/17.
 */
public interface RawEventRepo extends MongoRepository<RawEvent, String> {

    RawEvent findOneByCorrelationId(UUID correlationId);

    List<RawEvent> findAllByCorrelationId(UUID correlationId);

}
