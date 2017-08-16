package io.renthell.propertymgmtsrv.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventStoreConfiguration {

    @Value("${endpoints.event-store.uri-base}")
    private String EVENT_STORE_URI_BASE;

    @Value("${endpoints.event-store.add-property-command}")
    private String ADD_PROPERTY_COMMAND_PATH;

    public String addPropertyCommandUri() {
        return EVENT_STORE_URI_BASE + ADD_PROPERTY_COMMAND_PATH;
    }
}
