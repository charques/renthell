package io.renthell.scoringmgmtsrv.web.dto;

import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by cfhernandez on 24/8/17.
 */
@Setter
@Getter
@ToString
public class ScoringStatsDto {
    private ScoringDto scoring;

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

    public ScoringStatsDto(ScoringDto scoring) {
        this.scoring = scoring;
    }
}

