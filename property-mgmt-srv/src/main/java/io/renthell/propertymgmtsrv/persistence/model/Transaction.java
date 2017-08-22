package io.renthell.propertymgmtsrv.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by cfhernandez on 1/8/17.
 */
@Getter
@Setter
@ToString
public class Transaction {

    @NotNull
    private String transactionId;
    private String transaction;
    private Double price;
    private Double priceMin;
    private Double priceMax;
    private String priceRange;
}
