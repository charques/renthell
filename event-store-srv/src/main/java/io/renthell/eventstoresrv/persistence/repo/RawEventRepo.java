package io.renthell.eventstoresrv.persistence.repo;

import io.renthell.eventstoresrv.persistence.model.RawEvent;

/**
 * Created by cfhernandez on 10/7/17.
 */
public interface RawEventRepo {

    RawEvent findOne(String uuid);

    RawEvent save(RawEvent rawEvent);

}
