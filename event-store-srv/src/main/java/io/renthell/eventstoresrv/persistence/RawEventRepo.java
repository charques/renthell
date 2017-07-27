package io.renthell.eventstoresrv.persistence;

import io.renthell.eventstoresrv.common.persistence.event.RawEvent;

/**
 * Created by cfhernandez on 10/7/17.
 */
public interface RawEventRepo {

    RawEvent findOne(String uuid);

    RawEvent save(RawEvent rawEvent);

}
