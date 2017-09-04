package io.renthell.propertymgmtsrv.web.dto;

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
public class TransactionDto {

    @NotNull
    private String transactionId;
    private String transaction;
    private Float price;
    private Float priceMin;
    private Float priceMax;
    private String priceRange;
}
