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
    private String postalCode;

    private List<ScoringData> scoringDataList;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date modifiedDate;

    public Scoring addScoringDataItem(ScoringData data) {
        if(this.scoringDataList == null) {
            this.scoringDataList = new ArrayList<>();
        }
        this.scoringDataList.add(data);
        return this;
    }

}
