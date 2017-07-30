package io.renthell.crawlengine.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PropertyMgmtConfiguration {

    @Value("${endpoints.property-mgmt.uri-base}")
    private String propertyMgmtUriBase;

    @Value("${endpoints.property-mgmt.property-transaction}")
    private String propertyTransactionPath;
}
