package io.renthell.scoringmgmtsrv.service;

import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import io.renthell.scoringmgmtsrv.persistence.model.ScoringData;
import io.renthell.scoringmgmtsrv.web.dto.RangeDataDto;
import io.renthell.scoringmgmtsrv.web.dto.ScoringDto;
import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ScoringCalculationsHelper {

    @Autowired
    private ModelMapper modelMapper;

    public List<ScoringStatsDto> generateScoringStatsList(Boolean aggregate, String transactionId, Integer year,
                                                          Integer month, String postalCode, Integer rooms,
                                                          List<Scoring> scoringList) {
        if(aggregate) {
            Scoring aggregatedScoring = buildAggregateScoring(transactionId, month, year, postalCode, rooms, scoringList);
            scoringList.clear();
            scoringList.add(aggregatedScoring);
        }

        return buildScoringStatsList(scoringList, aggregate);
    }

    private List<ScoringStatsDto> buildScoringStatsList(List<Scoring> scoringList, Boolean aggregate) {
        List<ScoringStatsDto> statsList = new ArrayList<>();

        for (Scoring scoring : scoringList) {
            statsList.add(buildScoringStatsDto(scoring, aggregate));
        }
        return statsList;
    }

    private Scoring buildAggregateScoring(String transactionId, Integer year, Integer month,
                                          String postalCode, Integer rooms, List<Scoring> scoringList) {
        Scoring scoringResult = new Scoring();
        scoringResult.setTransactionId(transactionId);
        scoringResult.setYear(year);
        scoringResult.setMonth(month);
        scoringResult.setPostalCode(postalCode);
        scoringResult.setRooms(rooms);

        List<ScoringData> aggegatedScoringDataList = new ArrayList<>();
        for (Scoring scoring : scoringList) {
            aggegatedScoringDataList.addAll(scoring.getScoringDataList());
        }
        scoringResult.setScoringDataList(aggegatedScoringDataList);

        return scoringResult;
    }

    private ScoringStatsDto buildScoringStatsDto(Scoring scoring, Boolean aggregated) {
        ScoringDto scoringDto = modelMapper.map(scoring, ScoringDto.class);
        ScoringStatsDto scoringStats = new ScoringStatsDto(scoringDto);
        scoringStats.setAggregated(aggregated);

        // Prices x mts2 calculations
        DescriptiveStatistics statsPricesMts2 = new DescriptiveStatistics();
        DescriptiveStatistics statsPrices = new DescriptiveStatistics();
        for (ScoringData scoringData : scoring.getScoringDataList()) {
            statsPricesMts2.addValue(scoringData.getPrice()/scoringData.getMts2());
            statsPrices.addValue(scoringData.getPrice());
        }
        scoringStats.setPriceMts2Average(statsPricesMts2.getMean());
        scoringStats.setPriceMts2Median(statsPricesMts2.getPercentile(50D));
        scoringStats.setPriceAverage(statsPrices.getMean());
        scoringStats.setPriceMedian(statsPrices.getPercentile(50D));

        // Range calculations
        final Integer rangeStep = Ranges.getValueByTransactionId(scoring.getTransactionId());
        ArrayList<Integer> rangeIndexList = new ArrayList<>();
        // Get range indexes
        for (ScoringData scoringData : scoring.getScoringDataList()) {
            Float rangeIndex = scoringData.getPrice() / rangeStep;
            rangeIndexList.add((int) Math.ceil(rangeIndex));
        }

        // Generate ordered range tuples
        List<RangeTuple> rangeTupleList = new ArrayList<>();
        Set<Integer> rangeSet = new HashSet<Integer>(rangeIndexList);
        for (Integer key : rangeSet) {
            rangeTupleList.add(new RangeTuple(key, Collections.frequency(rangeIndexList, key)));
        }
        rangeTupleList.sort(new RangeTupleComparator());

        int numValues = scoring.getScoringDataList().size();
        if(rangeTupleList.size() > 0) {
            scoringStats.setFirstRange(buildRangeData(rangeTupleList.get(0), rangeStep, numValues));
        }
        if(rangeTupleList.size() > 1) {
            scoringStats.setSecondRange(buildRangeData(rangeTupleList.get(1), rangeStep, numValues));
        }
        return scoringStats;
    }

    private RangeDataDto buildRangeData(RangeTuple rangeTuple, int rangeStep, int numValues) {
        RangeDataDto rD = new RangeDataDto();
        int min = rangeStep * (rangeTuple.range-1) + 1;
        int max = rangeStep * rangeTuple.range;
        rD.setPercentage(rangeTuple.counter * 100 / numValues);
        rD.setRange("[" + String.valueOf(min) + "-" + String.valueOf(max) + "]");
        return rD;
    }

    class RangeTuple {
        private Integer range;
        private Integer counter;

        RangeTuple(Integer range, Integer counter) {
            this.range = range;
            this.counter = counter;
        }
    }

    class RangeTupleComparator implements Comparator<RangeTuple> {
        @Override
        public int compare(RangeTuple o1, RangeTuple o2) {
            if(o1.counter > o2.counter) {
                return -1;
            }
            return 1;
        }
    }

    enum Ranges{
        RANGE_SALE("1", 50000),
        RANGE_RENT("3", 200);

        static Integer getValueByTransactionId(String transactionId) {
            if(RANGE_RENT.transactionId.equals(transactionId)) {
                return RANGE_RENT.rangeValue;
            }
            else if(RANGE_SALE.transactionId.equals(transactionId)) {
                return RANGE_SALE.rangeValue;
            }
            return null;
        }

        private final String transactionId;
        private final Integer rangeValue;

        Ranges(String transactionId, Integer rangeValue){
            this.transactionId = transactionId;
            this.rangeValue = rangeValue;
        }
    }
}
