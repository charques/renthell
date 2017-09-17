package io.renthell.crawlengine.fotocasa.service;

import io.renthell.crawlengine.fotocasa.model.FotocasaItem;
import io.renthell.crawlengine.fotocasa.model.FotocasaTransactionItem;
import io.renthell.crawlengine.fotocasa.persistence.FotocasaRepository;
import io.renthell.crawlengine.propertymgmt.service.PropertyMgmtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by cfhernandez on 5/7/17.
 */
@Service
@Slf4j
public class FotocasaService {

    @Autowired
    private FotocasaRepository fotocasaRepository;

    @Autowired
    private PropertyMgmtService propertyMgmtService;


    public FotocasaItem saveUpdate(FotocasaItem itemToSave) {
        FotocasaItem itemRetrieved = fotocasaRepository.findOne(itemToSave.getId());
        FotocasaItem itemSaved = null;
        if (itemRetrieved == null) {
            // save new item
            itemToSave.setCreatedDate(new Date());
            itemSaved = fotocasaRepository.save(itemToSave);
            log.info("Saved OK: " + itemSaved.getWebUrl());

            // post ADD property transaction
            propertyMgmtService.addPropertyTransaction(itemSaved, 0);
        } else {
            Integer transactionIndex = getTransactionIndex(itemRetrieved, itemToSave);
            FotocasaTransactionItem transaction = itemToSave.getTransactions().get(0);
            if(shouldUpdate(itemRetrieved, itemToSave)) {
                if(transactionIndex < 0) {
                    // new transaction
                    List<FotocasaTransactionItem> transactions = itemRetrieved.getTransactions();
                    transactions.add(transaction);
                    itemRetrieved.setTransactions(transactions);
                    transactionIndex = transactions.size()-1;
                }
                else {
                    // update transaction
                    List<FotocasaTransactionItem> transactions = itemRetrieved.getTransactions();
                    transactions.set(transactionIndex, transaction);
                    itemRetrieved.setTransactions(transactions);
                }
                itemRetrieved.setModifiedDate(new Date());
                itemRetrieved.setUpdated(true);
                // update item
                itemSaved = fotocasaRepository.save(itemRetrieved);
                log.info("Updated OK: " + itemSaved.getWebUrl());

                // post UPDATED property transaction event
                propertyMgmtService.addPropertyTransaction(itemSaved, transactionIndex);
            }
            else {
                log.info("No action: " + itemRetrieved.getWebUrl());
            }
        }
        return itemSaved;
    }

    private Integer getTransactionIndex(FotocasaItem savedItem, FotocasaItem itemToSave) {
        List<FotocasaTransactionItem> transactionItemList = savedItem.getTransactions();
        FotocasaTransactionItem transaction = itemToSave.getTransactions().get(0);
        for(int i = 0; i < transactionItemList.size(); i++) {
            FotocasaTransactionItem t = transactionItemList.get(i);
            if(t.getTransactionId().equals(transaction.getTransactionId())) {
                return i;
            }
        }
        return -1;
    }

    private Boolean shouldUpdate(FotocasaItem savedItem, FotocasaItem itemToSave) {
        Integer index = getTransactionIndex(savedItem, itemToSave);
        if(index >= 0) {
            FotocasaTransactionItem t1 = savedItem.getTransactions().get(index);
            FotocasaTransactionItem t2 = itemToSave.getTransactions().get(0);
            return !(t1.equals(t2));
        }
        return true;
    }

}
