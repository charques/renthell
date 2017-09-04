package io.renthell.alertmgmtsrv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by cfhernandez on 1/9/17.
 */
@Getter
@Setter
@ToString
public class TransactionDto {

    private String transactionId;
    private String transaction;
    private Float price;
    private Float priceMin;
    private Float priceMax;
    private String priceRange;
}
