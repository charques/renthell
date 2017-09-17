package io.renthell.alertmgmtsrv.web.eventhandler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import io.renthell.alertmgmtsrv.service.AlertService;
import io.renthell.alertmgmtsrv.web.dto.PropertyTransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventConsumer {

  @Autowired
  private AlertService alertService;

  @Autowired
  private ObjectMapper objectMapper;

  private final String PROPERTY_TRANSACTION_CONFIRM_EVENT = "io.renthell.eventstoresrv.web.events.PropertyTransactionConfirmEvent";

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

      if(PROPERTY_TRANSACTION_CONFIRM_EVENT.equals(payloadJson.get("type").textValue())) {
        PropertyTransactionDto propertyTransactionDto = buildPropertyDto(payload);
        alertService.evaluateProperty(propertyTransactionDto);

        log.info("Property transaction confirmed event processed.");
      }

      latch.countDown();

    } catch (IOException e) {
      log.error("Error parsing event payload {}", e.getLocalizedMessage());
    }

  }

  private PropertyTransactionDto buildPropertyDto(String payload) throws IOException {
    JsonNode payloadJson = objectMapper.readTree(payload);
    TextNode eventPayloadString = (TextNode) payloadJson.get("payload");
    JsonNode eventPayloadJson = objectMapper.readTree(eventPayloadString.textValue());

    String identifier = eventPayloadJson.get("identifier").textValue();
    String transactionId = eventPayloadJson.get("transactionId").textValue();

    PropertyTransactionDto propertyDto = new PropertyTransactionDto();
    propertyDto.setIdentifier(identifier);
    propertyDto.setTransactionId(transactionId);

    return propertyDto;
  }

}
