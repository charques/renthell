package io.renthell.scoringmgmtsrv.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cfhernandez on 23/8/17.
 */
@Getter
@Setter
@ToString
public class ScoringData {

    public ScoringData() {}

    public ScoringData(Float price, Integer mts2) {
        this.price = price;
        this.mts2 = mts2;
    }

    private Float price;
    private Integer mts2;
}
