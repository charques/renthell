package io.renthell.scoringmgmtsrv.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

@Setter
@Getter
@ToString
public class ScoringStats {
    private Scoring scoring;

    private Double average;
    private Double median;

    // dummy constructor for jackson
    public ScoringStats() {
    }

    public ScoringStats(Scoring scoring) {
        this.scoring = scoring;

        DescriptiveStatistics stats = new DescriptiveStatistics();
        List<Float> priceList = scoring.getPriceList();
        for (Float aPriceList : priceList) {
            stats.addValue(aPriceList.doubleValue());
        }

        this.average = stats.getMean();
        this.median = stats.getPercentile(50D);
    }
}
