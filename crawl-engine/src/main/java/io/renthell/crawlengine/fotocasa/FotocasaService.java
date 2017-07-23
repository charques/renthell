package io.renthell.crawlengine.fotocasa;

import io.renthell.crawlengine.trackingfeeder.TrackingFeederService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private TrackingFeederService trackingFeederService;


    public FotocasaItem saveUpdate(FotocasaItem itemToSave) {
        FotocasaItem itemRetrieved = fotocasaRepository.findOne(itemToSave.getId());
        FotocasaItem itemSaved = null;
        if (itemRetrieved == null) {
            // save new item
            itemSaved = fotocasaRepository.save(itemToSave);
            log.info("Saved: " + itemSaved.toString());

            // post ADD property transaction event
            trackingFeederService.addPropertyTransaction(itemSaved, 0);
        } else {
            Integer transactionIndex = getTransactionIndex(itemRetrieved, itemToSave);
            FotocasaTransactionItem transaction = itemToSave.getTransactions().get(0);
            if(shouldUpdate(itemRetrieved, itemToSave)) {
                if(transactionIndex < 0) {
                    // new transaction
                    List<FotocasaTransactionItem> transactions = itemRetrieved.getTransactions();
                    transactions.add(transaction);
                    itemToSave.setTransactions(transactions);
                }
                else {
                    // update transaction
                    List<FotocasaTransactionItem> transactions = itemRetrieved.getTransactions();
                    transactions.set(transactionIndex, transaction);
                    itemToSave.setTransactions(transactions);
                }
                itemToSave.setUpdated(true);
                // update item
                itemSaved = fotocasaRepository.save(itemToSave);
                log.info("Updated: " + itemSaved.toString());

                // post UPDATED property transaction event
                trackingFeederService.addPropertyTransaction(itemSaved, transactionIndex);
            }
            else {
                log.info("No update: " + itemRetrieved.toString());
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
        FotocasaTransactionItem t1 = savedItem.getTransactions().get(index);
        FotocasaTransactionItem t2 = itemToSave.getTransactions().get(0);
        return !(t1.equals(t2));
    }

}
