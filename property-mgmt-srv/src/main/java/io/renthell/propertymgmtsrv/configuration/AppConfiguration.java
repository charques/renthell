package io.renthell.propertymgmtsrv.configuration;

import io.renthell.propertymgmtsrv.api.exception.PropertyMgmtErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new PropertyMgmtErrorHandler());
        return restTemplate;
    }

}
