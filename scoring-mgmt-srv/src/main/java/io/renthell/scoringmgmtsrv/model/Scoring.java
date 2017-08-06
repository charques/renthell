package io.renthell.scoringmgmtsrv.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cfhernandez on 6/8/17.
 */
@Getter
@Setter
@ToString
public class Scoring {

    private String transactionId;

    private Integer month;
    private Integer year;

    private String region;
    private String regionCode;
    private String postalCode;
    private String district;
    private String city;
    private Integer rooms;

    //private Float medianPrice;
    //private Float averagePrice;
    //private Float averageMts2Price;

    //private String range80;
    //private String range60;

    private List<Float> priceList;

    public Scoring addPrice(Float price) {
        if(this.priceList == null) {
            this.priceList = new ArrayList<>();
        }
        this.priceList.add(price);
        return this;
    }

}
