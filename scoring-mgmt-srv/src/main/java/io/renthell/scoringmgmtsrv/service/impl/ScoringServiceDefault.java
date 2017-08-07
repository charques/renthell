package io.renthell.scoringmgmtsrv.service.impl;

import io.renthell.scoringmgmtsrv.model.Property;
import io.renthell.scoringmgmtsrv.model.Scoring;
import io.renthell.scoringmgmtsrv.persistence.ScoringRepo;
import io.renthell.scoringmgmtsrv.service.ScoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * Created by cfhernandez on 6/8/17.
 */
@Service
@Slf4j
public class ScoringServiceDefault implements ScoringService {

    @Autowired
    private ScoringRepo scoringRepo;

    @Override
    public Scoring addPropertyToScoring(Property property) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(property.getDate());
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        Scoring scoringRetrieved = scoringRepo.findOne(property.getTransactionId(),
                month, year, property.getPostalCode(),
                property.getDistrict(), property.getCity(), property.getRooms());

        Scoring scoringSaved = null;
        if (scoringRetrieved == null) {
            Scoring scoring = new Scoring();
            scoring.setTransactionId(property.getTransactionId());
            scoring.setMonth(month);
            scoring.setYear(year);
            scoring.setRegion(property.getRegion());
            scoring.setRegionCode(property.getPostalCode().substring(0,2));
            scoring.setPostalCode(property.getPostalCode());
            scoring.setDistrict(property.getDistrict());
            scoring.setCity(property.getCity());
            scoring.setRooms(property.getRooms());
            scoring.addPrice(property.getPrice());

            // save new item
            scoringSaved = scoringRepo.save(scoring);
            log.info("Saved: " + scoringSaved.toString());

        } else {
            scoringRetrieved.addPrice(property.getPrice());
            // save updated item
            scoringSaved = scoringRepo.save(scoringRetrieved);
            log.info("Updated: " + scoringSaved.toString());
        }

        return scoringSaved;
    }

    @Override
    public List<Scoring> findAll() {
        return scoringRepo.findAll();
    }

}
