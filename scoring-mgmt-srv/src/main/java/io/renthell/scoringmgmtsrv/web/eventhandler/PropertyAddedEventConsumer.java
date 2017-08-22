package io.renthell.scoringmgmtsrv.web.eventhandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import io.renthell.scoringmgmtsrv.web.dto.PropertyDto;
import io.renthell.scoringmgmtsrv.service.ScoringService;
import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import io.renthell.scoringmgmtsrv.web.exception.EventProcesingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PropertyAddedEventConsumer {

  @Autowired
  private ScoringService scoringService;

  @Autowired
  private ObjectMapper objectMapper;

  private final String PROPERTY_TRANSACTION_ADDED_EVENT = "io.renthell.eventstoresrv.web.events.PropertyTransactionAddedEvent";

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  @KafkaListener(topics = "${kafka.topic.events}")
  public void consumeEvent(String payload) {
    log.info("Event consumed. Event payload='{}'", payload);

    PropertyDto propertyDto = null;
    try {
      JsonNode payloadJson = objectMapper.readTree(payload);
      TextNode eventPayloadString = (TextNode) payloadJson.get("payload");
      JsonNode eventPayloadJson = objectMapper.readTree(eventPayloadString.textValue());

      if(PROPERTY_TRANSACTION_ADDED_EVENT.equals(payloadJson.get("type").textValue())) {
        propertyDto = buildPropertyDto(eventPayloadJson);
        ScoringStatsDto scoringStatsDto = scoringService.addPropertyToScoring(propertyDto);
        log.info("Property transaction added event processed. Scoring updated: {}", scoringStatsDto.toString());
      }

    } catch (IOException | ParseException e) {
      log.error("Error parsing event payload {}", e.getLocalizedMessage());
      throw new EventProcesingException(e);
    }

    latch.countDown();
  }

  private PropertyDto buildPropertyDto(JsonNode eventPayloadJson) throws ParseException {
    String transactionId = eventPayloadJson.get("transactionId").textValue();
    String publishDateString = eventPayloadJson.get("publishDate").textValue();
    Date date = new Date();
    if (publishDateString != null) {
      DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      date = format.parse(publishDateString);
    }
    String region = eventPayloadJson.get("region").textValue().toLowerCase();
    String postalCode = eventPayloadJson.get("postalCode").textValue();
    String district = eventPayloadJson.get("district").textValue().toLowerCase();
    String city = eventPayloadJson.get("city").textValue().toLowerCase();
    int rooms = Integer.parseInt(eventPayloadJson.get("rooms").textValue());
    Float price = Float.parseFloat(eventPayloadJson.get("price").textValue());

    PropertyDto propertyDto = new PropertyDto();
    propertyDto.setTransactionId(transactionId);
    propertyDto.setDate(date);
    propertyDto.setRegion(region);
    propertyDto.setPostalCode(postalCode);
    propertyDto.setDistrict(district);
    propertyDto.setCity(city);
    propertyDto.setRooms(rooms);
    propertyDto.setPrice(price);

    return propertyDto;
  }
}
