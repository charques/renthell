package io.renthell.alertmgmtsrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.alertmgmtsrv.configuration.PropertyMgmtConfiguration;
import io.renthell.alertmgmtsrv.configuration.ScoringMgmtConfiguration;
import io.renthell.alertmgmtsrv.persistence.model.Alert;
import io.renthell.alertmgmtsrv.persistence.repo.AlertRepo;
import io.renthell.alertmgmtsrv.service.rulesengine.PropertyRulesEngine;
import io.renthell.alertmgmtsrv.web.dto.AlertDto;
import io.renthell.alertmgmtsrv.web.dto.PropertyDto;
import io.renthell.alertmgmtsrv.web.dto.PropertyTransactionDto;
import io.renthell.alertmgmtsrv.web.dto.ScoringStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by cfhernandez on 28/8/17.
 */
@Service
@Slf4j
public class AlertServiceDefault implements AlertService {

    @Autowired
    private AlertRepo alertRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertyMgmtConfiguration propertyMgmtConfiguration;

    @Autowired
    private ScoringMgmtConfiguration scoringMgmtConfiguration;

    @Autowired
    private PropertyRulesEngine propertyRulesEngine;

    @Autowired
    private EventStoreService eventStoreService;

    @Override
    public AlertDto evaluateProperty(PropertyTransactionDto propertyTransactionDto) throws JsonProcessingException {
        PropertyDto propertyDto = getPropertyDetails(propertyTransactionDto.getIdentifier());

        ScoringStatsDto scoringStatsDto = getScoringStats(propertyTransactionDto.getTransactionId(),
                propertyDto.getPublishDate(), propertyDto.getPostalCode());

        // check rules
        String ruleEngineResult = propertyRulesEngine.evaluateRules(propertyDto, scoringStatsDto);

        if(ruleEngineResult != null) {
            // save alert
            Alert alertSaved = save(ruleEngineResult, propertyTransactionDto.getIdentifier(), propertyTransactionDto.getTransactionId());

            // add alert event
            AlertDto alertDto = buildAlertDto(alertSaved);
            eventStoreService.addAlertEvent(alertDto);
            return alertDto;
        }
        return null;
    }

    @Override
    public List<AlertDto> findAll() {
        List<Alert> properties = alertRepo.findAll();
        return buildAlertDtoList(properties);
    }

    @Override
    public AlertDto findOne(String id) {
        Alert alert = alertRepo.findOne(id);
        return (alert != null) ? buildAlertDto(alert) : null;
    }

    private Alert save(String alertDescriptor, String propertyId, String transactionId) {
        Alert alertToSave = new Alert();
        alertToSave.setAlertDescriptor(alertDescriptor);
        alertToSave.setPropertyId(propertyId);
        alertToSave.setTransactionId(transactionId);
        return alertRepo.save(alertToSave);
    }

    private PropertyDto getPropertyDetails(String propertyIdentifier) {

        String urlString = propertyMgmtConfiguration.getPropertyUri();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlString);

        Map<String, String> uriParams = new HashMap<String, String>();
        uriParams.put("id", propertyIdentifier);

        URI uri = builder.buildAndExpand(uriParams).toUri();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        PropertyDto property = restTemplate.getForObject(uri, PropertyDto.class);
        log.info("Property retrieved: {}", property.toString());
        return property;
    }

    private ScoringStatsDto getScoringStats(String transactionId, Date publishDate, String postalCode) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(publishDate);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String urlString = scoringMgmtConfiguration.getScoringUri();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlString)
                .queryParam("aggregate", "false")
                .queryParam("transactionId", transactionId)
                .queryParam("month", month)
                .queryParam("year", year)
                .queryParam("postalCode", postalCode);

        URI uri = builder.build().toUri();

        ScoringStatsDto scoringStatsDto = restTemplate.getForObject(uri, ScoringStatsDto.class);
        log.info("Scoring stats retrieved: {}", scoringStatsDto.toString());

        return scoringStatsDto;
    }

    private List<AlertDto> buildAlertDtoList(List<Alert> alertlist) {
        java.lang.reflect.Type targetListType = new TypeToken<List<AlertDto>>() {}.getType();
        List<AlertDto> alertDtos = modelMapper.map(alertlist, targetListType);
        return alertDtos;
    }

    private AlertDto buildAlertDto(Alert alert) {
        return modelMapper.map(alert, AlertDto.class);
    }
}
