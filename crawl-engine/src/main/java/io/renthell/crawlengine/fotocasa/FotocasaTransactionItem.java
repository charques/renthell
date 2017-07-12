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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FotocasaTransactionItem that = (FotocasaTransactionItem) o;

        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (transaction != null ? !transaction.equals(that.transaction) : that.transaction != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (priceMax != null ? !priceMax.equals(that.priceMax) : that.priceMax != null) return false;
        if (priceMin != null ? !priceMin.equals(that.priceMin) : that.priceMin != null) return false;
        if (priceRange != null ? !priceRange.equals(that.priceRange) : that.priceRange != null) return false;
        return priceOas != null ? priceOas.equals(that.priceOas) : that.priceOas == null;
    }

}
