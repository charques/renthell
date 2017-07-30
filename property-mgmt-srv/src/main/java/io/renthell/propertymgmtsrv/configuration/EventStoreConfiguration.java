package io.renthell.propertymgmtsrv.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EventStoreConfiguration {

    @Value("${endpoints.event-store.uri-base}")
    private String eventStoreUriBase;

    @Value("${endpoints.event-store.add-property-command}")
    private String addPropertyCommandPath;
}
