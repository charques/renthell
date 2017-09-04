package io.renthell.alertmgmtsrv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by cfhernandez on 28/8/17.
 */
@Getter
@Setter
@ToString
public class PropertyTransactionDto {

    private String identifier;
    private String transactionId;

}
