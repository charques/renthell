package io.renthell.crawlengine.fotocasa;

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

    public FotocasaItem checkAndSaveItem(FotocasaItem item) throws InterruptedException, TransactionAlreadySavedException {
        FotocasaItem findItem = fotocasaRepository.findOne(item.getId());

        // add or update transactions
        if(findItem != null) {
            List<FotocasaTransactionItem> transactionItemList = findItem.getTransactions();

            transactionItemList = getTransactionIndex(transactionItemList, item.getTransactions().get(0));
            item.setTransactions(transactionItemList);
        }

        final FotocasaItem itemSaved = fotocasaRepository.save(item);
        log.info("Saved " + itemSaved.toString());
        return itemSaved;
    }

    private List<FotocasaTransactionItem> getTransactionIndex(List<FotocasaTransactionItem> transactionItemList, FotocasaTransactionItem transaction) throws TransactionAlreadySavedException {
        for(int i = 0; i < transactionItemList.size(); i++) {
            FotocasaTransactionItem t = transactionItemList.get(i);
            if(transaction.equals(t)) {
                throw new TransactionAlreadySavedException();
            }
            else if(t.getTransactionId().equals(transaction.getTransactionId())) {
                transactionItemList.set(i, transaction);
                return transactionItemList;
            }
        }
        transactionItemList.add(transaction);
        return transactionItemList;
    }

    public class TransactionAlreadySavedException extends Exception {}
}
