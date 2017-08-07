package io.renthell.scoringmgmtsrv.eventhandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import io.renthell.scoringmgmtsrv.exception.ScoringMgmtException;
import io.renthell.scoringmgmtsrv.model.Property;
import io.renthell.scoringmgmtsrv.model.Scoring;
import io.renthell.scoringmgmtsrv.service.ScoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import io.renthell.scoringmgmtsrv.exception.ScoringMgmtException.ErrorCode;

@Slf4j
public class PropertyAddedEventConsumer {

  @Autowired
  private ScoringService scoringService;

  @Autowired
  private ObjectMapper objectMapper;

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  private final String PROPERTY_TRANSACTION_ADDED_EVENT = "io.renthell.eventstoresrv.events.PropertyTransactionAddedEvent";

  @KafkaListener(topics = "${kafka.topic.events}")
  public void consumeEvent(String payload) {
    log.info("Event consumed. Event payload='{}'", payload);

    Property property = null;
    try {
      JsonNode payloadJson = objectMapper.readTree(payload);
      TextNode eventPayloadString = (TextNode) payloadJson.get("payload");
      JsonNode eventPayloadJson = objectMapper.readTree(eventPayloadString.textValue());

      if(PROPERTY_TRANSACTION_ADDED_EVENT.equals(payloadJson.get("type").textValue())) {
        property = buildProperty(eventPayloadJson);
        Scoring scoring = scoringService.addPropertyToScoring(property);
        log.info("Property transaction added event processed. Scoring updated: {}", scoring.toString());
      }

    } catch (IOException | ParseException e) {
      throw new ScoringMgmtException(ErrorCode.PARSE_ERROR, "Error parsing event payload", e);
    }

    latch.countDown();
  }

  private Property buildProperty(JsonNode eventPayloadJson) throws ParseException {
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

    Property property = new Property();
    property.setTransactionId(transactionId);
    property.setDate(date);
    property.setRegion(region);
    property.setPostalCode(postalCode);
    property.setDistrict(district);
    property.setCity(city);
    property.setRooms(rooms);
    property.setPrice(price);

    return property;
  }
}
