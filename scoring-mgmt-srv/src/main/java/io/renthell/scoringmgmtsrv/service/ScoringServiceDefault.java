package io.renthell.scoringmgmtsrv.service;

import io.renthell.scoringmgmtsrv.persistence.model.ScoringData;
import io.renthell.scoringmgmtsrv.web.dto.PropertyDto;
import io.renthell.scoringmgmtsrv.persistence.model.Scoring;
import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import io.renthell.scoringmgmtsrv.persistence.repo.ScoringRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by cfhernandez on 6/8/17.
 */
@Service
@Slf4j
public class ScoringServiceDefault implements ScoringService {

    @Autowired
    private ScoringRepo scoringRepo;

    @Override
    public ScoringStatsDto addPropertyToScoring(PropertyDto propertyDto) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(propertyDto.getDate());
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        List<Scoring> scoringList = scoringRepo.find(propertyDto.getTransactionId(),
                month, year, propertyDto.getPostalCode());

        Scoring scoringRetrieved = null;
        if(scoringList.size() > 0) {
            scoringRetrieved = scoringList.get(0);
        }

        Scoring scoringSaved = null;
        if (scoringRetrieved == null) {
            Scoring scoring = new Scoring();
            scoring.setTransactionId(propertyDto.getTransactionId());
            scoring.setMonth(month);
            scoring.setYear(year);
            scoring.setPostalCode(propertyDto.getPostalCode());

            ScoringData scoringData = new ScoringData(propertyDto.getPrice(), propertyDto.getMts2(), propertyDto.getRooms());
            scoring.addScoringDataItem(scoringData);

            // save new item
            scoringSaved = scoringRepo.save(scoring);
            log.info("Saved: " + scoringSaved.toString());

        } else {
            ScoringData scoringData = new ScoringData(propertyDto.getPrice(), propertyDto.getMts2(), propertyDto.getRooms());
            scoringRetrieved.addScoringDataItem(scoringData);

            // save updated item
            scoringSaved = scoringRepo.save(scoringRetrieved);
            log.info("Updated: " + scoringSaved.toString());
        }
        ScoringStatsDto result = new ScoringStatsDto(scoringSaved);
        return result;
    }

    @Override
    public List<ScoringStatsDto> findAll() {
        List<Scoring> scoringList = scoringRepo.findAll();
        List<ScoringStatsDto> statsList = new ArrayList<>();
        for (Scoring aScoringList : scoringList) {
            ScoringStatsDto stats = new ScoringStatsDto(aScoringList);
            statsList.add(stats);
        }
        return statsList;
    }

    @Override
    public List<ScoringStatsDto> find(Boolean aggregate, String transactionId, Integer year, Integer month, String postalCode) {
        List<Scoring> scoringList = scoringRepo.find(transactionId, month, year, postalCode);

        return ScoringCalculationsHelper.generateScoringStatsList(aggregate, transactionId, year, month, postalCode, scoringList);
    }

}
