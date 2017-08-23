package io.renthell.propertymgmtsrv.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by cfhernandez on 1/8/17.
 */
@Getter
@Setter
@ToString
@Document(collection = "property")
public class Property {

    @Id
    private String id;

    @NotNull
    private String identifier;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date publishDate;

    private String region;
    private String regionCode;
    private String city;
    private String district;
    private String neighbourhood;
    private String street;
    private String postalCode;

    private String property;
    private String propertySub;
    private String propertyState;
    private String propertyType;

    private int mts2;
    private String rooms;
    private String bathrooms;
    private String heating;
    private String energeticCert;

    private String features;

    private String lat;
    private String lng;
    private String feed;

    private List<Transaction> transactions;

    private Boolean updated;

    private Calculations calculations;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date modifiedDate;

    public void updateCalculations() {
        final String SALE = "1";
        final String RENT = "3";

        if(this.calculations == null) {
            this.calculations = new Calculations();
        }

        if(this.transactions != null && this.transactions.size() > 0) {
            Transaction saleTransaction = null;
            Transaction rentTransaction = null;
            for (Transaction transaction : this.transactions) {
                if(SALE.equals(transaction.getTransactionId())) {
                    saleTransaction = transaction;

                    this.calculations.setSaleMt2Price(saleTransaction.getPrice() / this.mts2);
                }
                else if(RENT.equals(transaction.getTransactionId())) {
                    rentTransaction = transaction;

                    this.calculations.setRentMt2Price(rentTransaction.getPrice() / this.mts2);
                }

                if(saleTransaction != null && rentTransaction != null) {
                    this.calculations.setRentGrossReturn(rentTransaction.getPrice() / saleTransaction.getPrice());
                    this.calculations.setRentPer(saleTransaction.getPrice() / rentTransaction.getPrice());
                    break;
                }
            }

        }
    }
}
