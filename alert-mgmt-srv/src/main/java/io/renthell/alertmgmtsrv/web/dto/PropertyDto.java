package io.renthell.alertmgmtsrv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Created by cfhernandez on 28/8/17.
 */
@Getter
@Setter
@ToString
public class PropertyDto {

    private String identifier;
    private Date publishDate;
    private String postalCode;

    private List<TransactionDto> transactions;

    private CalculationsDTO calculations;

}
