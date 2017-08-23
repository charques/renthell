package io.renthell.propertymgmtsrv.service;

import io.renthell.propertymgmtsrv.web.dto.PropertyDto;
import io.renthell.propertymgmtsrv.persistence.model.Property;
import io.renthell.propertymgmtsrv.persistence.model.Transaction;
import io.renthell.propertymgmtsrv.persistence.repo.PropertyRepo;
import io.renthell.propertymgmtsrv.web.dto.TransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private ModelMapper modelMapper;

    @Override
    public PropertyDto save(PropertyDto propertyDto) throws ParseException {
        log.info("Saving property");

        Property propertyRetrieved = propertyRepo.findOne(propertyDto.getIdentifier());
        Property propertySaved = null;
        if (propertyRetrieved == null) {
            // save new item
            Property propertyToSave = buildProperty(propertyDto);
            propertySaved = propertyRepo.save(propertyToSave);
            log.info("Property saved: " + propertySaved.toString());

        } else {
            TransactionDto transactionDto = propertyDto.getTransactions().get(0);
            Integer transactionIndex = getTransactionIndex(propertyRetrieved, transactionDto.getTransactionId());
            Transaction transaction = buildTransaction(transactionDto);

            if(transactionIndex < 0) {
                // new transaction
                List<Transaction> transactions = propertyRetrieved.getTransactions();
                transactions.add(transaction);
                propertyRetrieved.setTransactions(transactions);
            }
            else {
                // update transaction
                List<Transaction> transactions = propertyRetrieved.getTransactions();
                transactions.set(transactionIndex, transaction);
                propertyRetrieved.setTransactions(transactions);
            }
            // update calculations
            propertyRetrieved.updateCalculations();

            // update item
            propertySaved = propertyRepo.save(propertyRetrieved);
            log.info("Property updated: " + propertySaved.toString());
        }

        return buildPropertyDto(propertySaved);
    }

    @Override
    public List<PropertyDto> findAll() {
        List<Property> properties = propertyRepo.findAll();
        return buildPropertyDtoList(properties);
    }

    @Override
    public PropertyDto findOne(String id) {
        Property property = propertyRepo.findOne(id);
        return (property != null) ? buildPropertyDto(property) : null;
    }

    private List<PropertyDto> buildPropertyDtoList(List<Property> propertylist) {
        java.lang.reflect.Type targetListType = new TypeToken<List<PropertyDto>>() {}.getType();
        List<PropertyDto> propertyDtos = modelMapper.map(propertylist, targetListType);
        return propertyDtos;
    }

    private PropertyDto buildPropertyDto(Property property) {
        return modelMapper.map(property, PropertyDto.class);
    }

    private Property buildProperty(PropertyDto property) {
        return modelMapper.map(property, Property.class);
    }

    private Transaction buildTransaction(TransactionDto transactionDto) {
        return modelMapper.map(transactionDto, Transaction.class);
    }

    private Integer getTransactionIndex(Property savedItem, String transactionId) {
        List<Transaction> transactionItemList = savedItem.getTransactions();
        for(int i = 0; i < transactionItemList.size(); i++) {
            Transaction t = transactionItemList.get(i);
            if(t.getTransactionId().equals(transactionId)) {
                return i;
            }
        }
        return -1;
    }
}
