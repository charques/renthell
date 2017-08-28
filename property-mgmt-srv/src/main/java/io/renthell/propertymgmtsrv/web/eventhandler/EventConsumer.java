package io.renthell.propertymgmtsrv.web.eventhandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import io.renthell.propertymgmtsrv.web.dto.PropertyDto;
import io.renthell.propertymgmtsrv.web.dto.TransactionDto;
import io.renthell.propertymgmtsrv.web.exception.EventProcesingException;
import io.renthell.propertymgmtsrv.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class EventConsumer {

  @Autowired
  private PropertyService propertyService;

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
    log.info("Event consumed. Event payload='{}'", payload);

    PropertyDto propertyDto = null;
    try {
      JsonNode payloadJson = objectMapper.readTree(payload);
      TextNode eventPayloadString = (TextNode) payloadJson.get("payload");
      JsonNode eventPayloadJson = objectMapper.readTree(eventPayloadString.textValue());

      if(PROPERTY_TRANSACTION_ADD_EVENT.equals(payloadJson.get("type").textValue())) {
        propertyDto = buildProperty(eventPayloadJson);
        PropertyDto propertySaved = propertyService.save(propertyDto);
        log.info("Property transaction added event processed. Property saved: {}", propertySaved.toString());
      }

    } catch (IOException | ParseException e) {
      log.error("Error parsing event payload {}", e.getLocalizedMessage());
      throw new EventProcesingException(e);
    }

    latch.countDown();
  }

  private PropertyDto buildProperty(JsonNode eventPayloadJson) throws ParseException {

    final PropertyDto propertyDto = new PropertyDto();
    propertyDto.setIdentifier(eventPayloadJson.get("identifier").textValue());
    String publishDateString = eventPayloadJson.get("publishDate").textValue();
    Date date = new Date();
    if (publishDateString != null) {
      DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      date = format.parse(publishDateString);
    }
    propertyDto.setPublishDate(date);

    propertyDto.setRegion(eventPayloadJson.get("region").textValue());
    propertyDto.setCity(eventPayloadJson.get("city").textValue());
    propertyDto.setDistrict(eventPayloadJson.get("district").textValue());
    propertyDto.setNeighbourhood(eventPayloadJson.get("neighbourhood").textValue());
    propertyDto.setStreet(eventPayloadJson.get("street").textValue());
    String postalCodeString = eventPayloadJson.get("postalCode").textValue();
    propertyDto.setPostalCode(postalCodeString);
    if(postalCodeString != null && postalCodeString.length() > 2) {
      propertyDto.setRegionCode(postalCodeString.substring(0,2));
    }
    propertyDto.setProperty(eventPayloadJson.get("property").textValue());
    propertyDto.setPropertySub(eventPayloadJson.get("propertySub").textValue());
    propertyDto.setPropertyState(eventPayloadJson.get("propertyState").textValue());
    propertyDto.setPropertyType(eventPayloadJson.get("propertyType").textValue());
    propertyDto.setMts2(eventPayloadJson.get("mts2").textValue());
    propertyDto.setRooms(eventPayloadJson.get("rooms").textValue());
    propertyDto.setBathrooms(eventPayloadJson.get("bathrooms").textValue());
    propertyDto.setHeating(eventPayloadJson.get("heating").textValue());
    propertyDto.setEnergeticCert(eventPayloadJson.get("energeticCert").textValue());
    propertyDto.setFeatures(eventPayloadJson.get("features").textValue());
    propertyDto.setLat(eventPayloadJson.get("lat").textValue());
    propertyDto.setLng(eventPayloadJson.get("lng").textValue());
    propertyDto.setFeed(eventPayloadJson.get("feed").textValue());

    final TransactionDto transactionDto = new TransactionDto();
    transactionDto.setTransactionId(eventPayloadJson.get("transactionId").textValue());
    transactionDto.setTransaction(eventPayloadJson.get("transaction").textValue());
    transactionDto.setPrice(eventPayloadJson.get("price").textValue());
    transactionDto.setPriceMin(eventPayloadJson.get("priceMin").textValue());
    transactionDto.setPriceMax(eventPayloadJson.get("priceMax").textValue());
    transactionDto.setPriceRange(eventPayloadJson.get("priceRange").textValue());

    List<TransactionDto> transactions = new ArrayList<>();
    transactions.add(transactionDto);
    propertyDto.setTransactions(transactions);

    return propertyDto;
  }
}
