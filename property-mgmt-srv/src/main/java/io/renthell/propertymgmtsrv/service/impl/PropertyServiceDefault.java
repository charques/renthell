package io.renthell.propertymgmtsrv.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renthell.propertymgmtsrv.exception.PropertyMgmtException;
import io.renthell.propertymgmtsrv.model.Property;
import io.renthell.propertymgmtsrv.model.Transaction;
import io.renthell.propertymgmtsrv.persistence.PropertyRepo;
import io.renthell.propertymgmtsrv.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cfhernandez on 11/7/17.
 */
@Service
@Slf4j
public class PropertyServiceDefault implements PropertyService {

    @Autowired
    private PropertyRepo propertyRepo;

    @Override
    public Property save(Property propertyToSave) throws PropertyMgmtException {
        log.info("Saving property");

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
                propertyRetrieved.setTransactions(transactions);
            }
            else {
                // update transaction
                List<Transaction> transactions = propertyRetrieved.getTransactions();
                transactions.set(transactionIndex, transaction);
                propertyRetrieved.setTransactions(transactions);
            }
            // update item
            propertySaved = propertyRepo.save(propertyRetrieved);
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
}
