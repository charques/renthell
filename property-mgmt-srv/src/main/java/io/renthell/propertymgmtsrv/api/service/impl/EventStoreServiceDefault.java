package io.renthell.propertymgmtsrv.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.propertymgmtsrv.configuration.EventStoreConfiguration;
import io.renthell.propertymgmtsrv.api.model.PropertyTransactionApi;
import io.renthell.propertymgmtsrv.api.service.EventStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * Created by cfhernandez on 11/7/17.
 */
@Service
@Slf4j
public class EventStoreServiceDefault implements EventStoreService {

    @Autowired
    private EventStoreConfiguration eventStoreConfiguration;

    @Autowired
    private ObjectMapper objectMapper;

    public Boolean addPropertyTransaction(PropertyTransactionApi item) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            log.error("Adding property transaction: KO {}", item);
            return false;
        }

        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String urlString = eventStoreConfiguration.getEventStoreUriBase() + eventStoreConfiguration.getAddPropertyCommandPath();

        ResponseEntity<String> pTransactionResponse = restTemplate.exchange(urlString, HttpMethod.POST, entity, String.class);

        if (pTransactionResponse.getStatusCode() == HttpStatus.CREATED) {
            log.info("Adding property transaction: OK");
            return true;
        } else {
            log.error("Adding property transaction: KO {}", pTransactionResponse.getStatusCode());
            return false;
        }
    }
}
