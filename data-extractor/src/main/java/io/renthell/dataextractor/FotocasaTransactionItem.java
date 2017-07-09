package io.renthell.dataextractor;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cfhernandez on 9/7/17.
 */
@Getter
@Setter
public class FotocasaTransactionItem {
    private String transactionId;
    private String transaction;
    private String price;
    private String priceMax;
    private String priceMin;
    private String priceRange;
    private String priceOas;
}
