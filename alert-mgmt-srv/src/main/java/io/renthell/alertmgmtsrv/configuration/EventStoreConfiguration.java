package io.renthell.alertmgmtsrv.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventStoreConfiguration {

    @Value("${endpoints.event-store.uri-base}")
    private String EVENT_STORE_URI_BASE;

    @Value("${endpoints.event-store.add-alert-command}")
    private String ADD_ALERT_COMMAND_PATH;

    public String addAlertCommandUri() {
        return EVENT_STORE_URI_BASE + ADD_ALERT_COMMAND_PATH;
    }

}
