package io.renthell.scoringmgmtsrv.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Created by cfhernandez on 6/8/17.
 */
@Getter
@Setter
@ToString
public class Property {

    private String transactionId;

    private Date date;

    private String region;
    private String postalCode;
    private String district;
    private String city;
    private Integer rooms;

    private Float price;

}
