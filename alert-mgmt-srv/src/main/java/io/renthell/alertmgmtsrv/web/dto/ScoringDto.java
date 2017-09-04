package io.renthell.alertmgmtsrv.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cfhernandez on 1/9/17.
 */
@Getter
@Setter
@ToString
public class ScoringDto {

    private String transactionId;
    private Integer month;
    private Integer year;
    private String postalCode;

}
