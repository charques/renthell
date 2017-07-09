package io.renthell.crawlengine;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by cfhernandez on 5/7/17.
 */
@Service
@Slf4j
public class FotocasaService {

    @Autowired
    private FotocasaRepository fotocasaRepository;

    public CompletableFuture<FotocasaItem> saveItem(FotocasaItem item) throws InterruptedException {
        log.info("Saving " + item.toString());

        FotocasaItem findItem = fotocasaRepository.findOne(item.getId());

        // add or update transactions
        if(findItem != null) {
            FotocasaTransactionItem currentTransaction = item.getTransactions().get(0);
            List<FotocasaTransactionItem> transactionItemList = findItem.getTransactions();
            int index = getTransactionIndex(transactionItemList, currentTransaction);
            if (index >= 0) {
                // update transaction
                transactionItemList.set(index, currentTransaction);
            }
            else {
                // new transaction
                transactionItemList.add(currentTransaction);
            }

            item.setTransactions(transactionItemList);
        }

        final FotocasaItem itemSaved = fotocasaRepository.save(item);
        log.info("Saved " + itemSaved.toString());
        return CompletableFuture.completedFuture(itemSaved);
    }

    private int getTransactionIndex(List<FotocasaTransactionItem> transactionItemList, FotocasaTransactionItem transaction) {
        for(int i = 0; i < transactionItemList.size(); i++) {
            FotocasaTransactionItem t = transactionItemList.get(i);
            if(t.getTransactionId().equals(transaction.getTransactionId())) {
                return i;
            }
        }
        return -1;
    }
}
