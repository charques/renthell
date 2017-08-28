package io.renthell.scoringmgmtsrv.web.dto;

import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import io.renthell.scoringmgmtsrv.persistence.model.ScoringData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.text.DecimalFormat;
import java.util.*;

@Setter
@Getter
@ToString
public class ScoringStatsDto {
    private Scoring scoring;

    private Double priceAverage;
    private Double priceMedian;

    private Double priceMts2Average;
    private Double priceMts2Median;

    private RangeDataDto firstRange;
    private RangeDataDto secondRange;

    private Boolean aggregated;

    // dummy constructor for jackson
    public ScoringStatsDto() {
    }

    public ScoringStatsDto(Scoring scoring) {
        this.scoring = scoring;
    }
}

