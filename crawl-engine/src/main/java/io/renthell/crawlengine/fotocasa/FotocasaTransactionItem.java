package io.renthell.crawlengine.fotocasa;

import lombok.*;

/**
 * Created by cfhernandez on 8/7/17.
 */
@Getter
@Setter
@Builder
public class FotocasaTransactionItem {

    private String transactionId;
    private String transaction;
    private String price;
    private String priceMax;
    private String priceMin;
    private String priceRange;
    private String priceOas;

}
