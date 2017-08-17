package io.renthell.scoringmgmtsrv.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cfhernandez on 6/8/17.
 */
@Getter
@Setter
@ToString
@Document(collection = "scoring")
public class Scoring {

    @Id
    private String id;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date modifiedDate;

    public Scoring addPrice(Float price) {
        if(this.priceList == null) {
            this.priceList = new ArrayList<>();
        }
        this.priceList.add(price);
        return this;
    }

}
