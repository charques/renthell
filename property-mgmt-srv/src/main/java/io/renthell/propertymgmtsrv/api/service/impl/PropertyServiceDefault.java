package io.renthell.propertymgmtsrv.api.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.propertymgmtsrv.api.exception.PropertyMgmtException;
import io.renthell.propertymgmtsrv.api.model.Property;
import io.renthell.propertymgmtsrv.api.model.Transaction;
import io.renthell.propertymgmtsrv.api.persistence.PropertyRepo;
import io.renthell.propertymgmtsrv.api.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cfhernandez on 11/7/17.
 */
@Service
@Slf4j
public class PropertyServiceDefault implements PropertyService {

    @Autowired
    private PropertyRepo propertyRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Property save(String propertyRawEventString) throws PropertyMgmtException {
        log.info("Saving property");
        Property propertyToSave = convert(propertyRawEventString);

        Property propertyRetrieved = propertyRepo.findOne(propertyToSave.getIdentifier());
        Property propertySaved = null;
        if (propertyRetrieved == null) {
            // save new item
            propertySaved = propertyRepo.save(propertyToSave);
            log.info("Saved: " + propertySaved.toString());

        } else {
            Integer transactionIndex = getTransactionIndex(propertyRetrieved, propertyToSave);
            Transaction transaction = propertyToSave.getTransactions().get(0);

            if(transactionIndex < 0) {
                // new transaction
                List<Transaction> transactions = propertyRetrieved.getTransactions();
                transactions.add(transaction);
                propertyToSave.setTransactions(transactions);
            }
            else {
                // update transaction
                List<Transaction> transactions = propertyRetrieved.getTransactions();
                transactions.set(transactionIndex, transaction);
                propertyToSave.setTransactions(transactions);
            }
            propertyToSave.setUpdated(true);
            // update item
            propertySaved = propertyRepo.save(propertyToSave);
            log.info("Updated: " + propertySaved.toString());
        }

        return propertySaved;
    }

    @Override
    public List<Property> findAll() {
        return propertyRepo.findAll();
    }

    @Override
    public Property findOne(String id) {
        return propertyRepo.findOne(id);
    }

    private Integer getTransactionIndex(Property savedItem, Property itemToSave) {
        List<Transaction> transactionItemList = savedItem.getTransactions();
        Transaction transaction = itemToSave.getTransactions().get(0);
        for(int i = 0; i < transactionItemList.size(); i++) {
            Transaction t = transactionItemList.get(i);
            if(t.getTransactionId().equals(transaction.getTransactionId())) {
                return i;
            }
        }
        return -1;
    }

    private Property convert(final String propertyRawEventString) throws PropertyMgmtException {
        JsonNode jsonProperty = null;
        try {
            jsonProperty = objectMapper.readTree(propertyRawEventString);
        } catch (IOException e) {
            throw new PropertyMgmtException(PropertyMgmtException.ErrorCode.PARSE_ERROR,
                    "Error deserializing the event: " + propertyRawEventString, e);
        }
        JsonNode jsonPayload = jsonProperty.get("payload");

        // TODO: como aseguro que el evento es un Property
        final Property property = new Property();
        property.setIdentifier(jsonPayload.get("identifier").asText());
        property.setRegion(jsonPayload.get("region").asText());
        property.setCity(jsonPayload.get("city").asText());
        property.setDistrict(jsonPayload.get("district").asText());
        property.setNeighbourhood(jsonPayload.get("neighbourhood").asText());
        property.setStreet(jsonPayload.get("street").asText());
        property.setPostalCode(jsonPayload.get("postalCode").asText());
        property.setProperty(jsonPayload.get("property").asText());
        property.setPropertySub(jsonPayload.get("propertySub").asText());
        property.setPropertyState(jsonPayload.get("propertyState").asText());
        property.setPropertyType(jsonPayload.get("propertyType").asText());
        property.setMts2(jsonPayload.get("mts2").asText());
        property.setRooms(jsonPayload.get("rooms").asText());
        property.setBathrooms(jsonPayload.get("bathrooms").asText());
        property.setHeating(jsonPayload.get("heating").asText());
        property.setEnergeticCert(jsonPayload.get("energeticCert").asText());
        property.setFeatures(jsonPayload.get("features").asText());
        property.setLat(jsonPayload.get("lat").asText());
        property.setLng(jsonPayload.get("lng").asText());
        property.setFeed(jsonPayload.get("feed").asText());

        final Transaction transaction = new Transaction();
        transaction.setTransactionId(jsonPayload.get("transactionId").asText());
        transaction.setTransaction(jsonPayload.get("transaction").asText());
        transaction.setPrice(jsonPayload.get("price").asText());
        transaction.setPriceMin(jsonPayload.get("priceMin").asText());
        transaction.setPriceMax(jsonPayload.get("priceMax").asText());
        transaction.setPriceRange(jsonPayload.get("priceRange").asText());

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        property.setTransactions(transactions);

        log.info(property.toString());

        return property;
    }
}
