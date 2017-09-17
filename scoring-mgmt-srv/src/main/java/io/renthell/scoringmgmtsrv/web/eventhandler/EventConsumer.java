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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventConsumer {

  @Autowired
  private ScoringService scoringService;

  @Autowired
  private ObjectMapper objectMapper;

  private final String PROPERTY_TRANSACTION_ADD_EVENT = "io.renthell.eventstoresrv.web.events.PropertyTransactionAddEvent";

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  public CountDownLatch setLatch(int counter) {
    latch = new CountDownLatch(counter);
    return latch;
  }

  @KafkaListener(topics = "${kafka.topic.events}")
  public void consumeEvent(String payload) {

    try {
      JsonNode payloadJson = objectMapper.readTree(payload);

      if(PROPERTY_TRANSACTION_ADD_EVENT.equals(payloadJson.get("type").textValue())) {
        PropertyDto propertyDto = buildPropertyDto(payload);
        ScoringStatsDto scoringStatsDto = scoringService.addPropertyToScoring(propertyDto);

        log.info("Property transaction added event processed. Scoring updated: {}", scoringStatsDto.toString());
      }

      latch.countDown();

    } catch (IOException | ParseException | NumberFormatException e) {
      log.error("Error parsing event payload {}", e.getLocalizedMessage());
    }

  }

  private PropertyDto buildPropertyDto(String payload) throws IOException, ParseException, NumberFormatException {
    JsonNode payloadJson = objectMapper.readTree(payload);
    TextNode eventPayloadString = (TextNode) payloadJson.get("payload");
    JsonNode eventPayloadJson = objectMapper.readTree(eventPayloadString.textValue());

    String transactionId = eventPayloadJson.get("transactionId").textValue();
    String publishDateString = eventPayloadJson.get("publishDate").textValue();
    Date date = new Date();
    if (publishDateString != null) {
      DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      date = format.parse(publishDateString);
    }
    String postalCode = eventPayloadJson.get("postalCode").textValue();
    int rooms = getInteger(eventPayloadJson.get("rooms").textValue());
    Float price = getFloat(eventPayloadJson.get("price").textValue());
    int mts2 = getInteger(eventPayloadJson.get("mts2").textValue());

    PropertyDto propertyDto = new PropertyDto();
    propertyDto.setTransactionId(transactionId);
    propertyDto.setDate(date);
    propertyDto.setPostalCode(postalCode);
    propertyDto.setRooms(rooms);
    propertyDto.setPrice(price);
    propertyDto.setMts2(mts2);

    return propertyDto;
  }

  private Float getFloat(String numberStr) {
    try{
      return Float.parseFloat(numberStr);
    }
    catch(NumberFormatException e) {
      return 0F;
    }
  }

  private Integer getInteger(String numberStr) {
    try{
      return Integer.parseInt(numberStr);
    }
    catch(NumberFormatException e) {
      return 0;
    }
  }

}
