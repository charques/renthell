package io.renthell.alertmgmtsrv.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyMgmtConfiguration {

    @Value("${endpoints.property-mgmt.uri-base}")
    private String PROPERTY_MGMT_URI_BASE;

    @Value("${endpoints.property-mgmt.get-property}")
    private String GET_PROPERTY_PATH;

    public String getPropertyUri() {
        return PROPERTY_MGMT_URI_BASE + GET_PROPERTY_PATH;
    }
}
