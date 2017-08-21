package io.renthell.propertymgmtsrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.propertymgmtsrv.configuration.EventStoreConfiguration;
import io.renthell.propertymgmtsrv.web.dto.PropertyInputDto;
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
    private RestTemplate restTemplate;

    @Autowired
    private EventStoreConfiguration eventStoreConfiguration;

    @Autowired
    private ObjectMapper objectMapper;

    public Boolean addPropertyTransaction(PropertyInputDto item) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(item);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String urlString = eventStoreConfiguration.addPropertyCommandUri();

        ResponseEntity<String> pTransactionResponse = null;
        pTransactionResponse = restTemplate.exchange(urlString, HttpMethod.POST, entity, String.class);
        log.info("Property transaction added: {}", pTransactionResponse.getStatusCodeValue());
        return true;
    }
}