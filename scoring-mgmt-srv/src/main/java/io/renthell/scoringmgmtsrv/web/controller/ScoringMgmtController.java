package io.renthell.scoringmgmtsrv.web.controller;

import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import io.renthell.scoringmgmtsrv.service.ScoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
@Slf4j
public class ScoringMgmtController {

    @Autowired
    private ScoringService scoringService;

    @RequestMapping(value = "/scoring-stats", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<ScoringStatsDto>> getAllScoringStats() {
        log.info("Get all Scoring Stats");

        return new ResponseEntity<>((Collection<ScoringStatsDto>) scoringService.findAll(), HttpStatus.OK);
    }

}
