package io.renthell.propertymgmtsrv.eventhandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import io.renthell.propertymgmtsrv.exception.PropertyMgmtException;
import io.renthell.propertymgmtsrv.exception.PropertyMgmtException.ErrorCode;
import io.renthell.propertymgmtsrv.model.Property;
import io.renthell.propertymgmtsrv.model.Transaction;
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
public class PropertyAddedEventConsumer {

  @Autowired
  private PropertyService propertyService;

  @Autowired
  private ObjectMapper objectMapper;

  private final String PROPERTY_TRANSACTION_ADDED_EVENT = "io.renthell.eventstoresrv.events.PropertyTransactionAddedEvent";

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

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
        Property propertySaved = propertyService.save(property);
        log.info("Property transaction added event processed. Scoring updated: {}", propertySaved.toString());
      }

    } catch (IOException | ParseException e) {
      throw new PropertyMgmtException(ErrorCode.PARSE_ERROR, "Error parsing event payload", e);
    }

    latch.countDown();
  }

  private Property buildProperty(JsonNode eventPayloadJson) throws ParseException {

    final Property property = new Property();
    String publishDateString = eventPayloadJson.get("publishDate").textValue();
    Date date = new Date();
    if (publishDateString != null) {
      DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      date = format.parse(publishDateString);
    }
    property.setPublishDate(date);
    property.setIdentifier(eventPayloadJson.get("identifier").textValue());
    property.setRegion(eventPayloadJson.get("region").textValue());
    property.setCity(eventPayloadJson.get("city").textValue());
    property.setDistrict(eventPayloadJson.get("district").textValue());
    property.setNeighbourhood(eventPayloadJson.get("neighbourhood").textValue());
    property.setStreet(eventPayloadJson.get("street").textValue());
    String postalCodeString = eventPayloadJson.get("postalCode").textValue();
    property.setPostalCode(postalCodeString);
    if(postalCodeString != null && postalCodeString.length() > 2) {
      property.setRegionId(postalCodeString.substring(0,2));
    }
    property.setProperty(eventPayloadJson.get("property").textValue());
    property.setPropertySub(eventPayloadJson.get("propertySub").textValue());
    property.setPropertyState(eventPayloadJson.get("propertyState").textValue());
    property.setPropertyType(eventPayloadJson.get("propertyType").textValue());
    property.setMts2(eventPayloadJson.get("mts2").textValue());
    property.setRooms(eventPayloadJson.get("rooms").textValue());
    property.setBathrooms(eventPayloadJson.get("bathrooms").textValue());
    property.setHeating(eventPayloadJson.get("heating").textValue());
    property.setEnergeticCert(eventPayloadJson.get("energeticCert").textValue());
    property.setFeatures(eventPayloadJson.get("features").textValue());
    property.setLat(eventPayloadJson.get("lat").textValue());
    property.setLng(eventPayloadJson.get("lng").textValue());
    property.setFeed(eventPayloadJson.get("feed").textValue());

    final Transaction transaction = new Transaction();
    transaction.setTransactionId(eventPayloadJson.get("transactionId").textValue());
    transaction.setTransaction(eventPayloadJson.get("transaction").textValue());
    transaction.setPrice(eventPayloadJson.get("price").textValue());
    transaction.setPriceMin(eventPayloadJson.get("priceMin").textValue());
    transaction.setPriceMax(eventPayloadJson.get("priceMax").textValue());
    transaction.setPriceRange(eventPayloadJson.get("priceRange").textValue());

    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction);
    property.setTransactions(transactions);

    log.info(property.toString());

    return property;
  }
}
