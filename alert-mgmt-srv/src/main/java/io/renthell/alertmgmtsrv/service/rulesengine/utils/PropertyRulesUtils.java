package io.renthell.alertmgmtsrv.service.rulesengine.utils;

import io.renthell.alertmgmtsrv.web.dto.PropertyDto;
import io.renthell.alertmgmtsrv.web.dto.TransactionDto;

import java.util.List;

public class PropertyRulesUtils {

    static public TransactionDto getTransaction(PropertyDto property, String transactionId) {
        List<TransactionDto> transactionItemList = property.getTransactions();
        for(TransactionDto transaction : transactionItemList) {
            if(transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }
}
