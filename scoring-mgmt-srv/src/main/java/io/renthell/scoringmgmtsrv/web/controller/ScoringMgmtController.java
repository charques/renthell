package io.renthell.scoringmgmtsrv.web.controller;

import io.renthell.scoringmgmtsrv.web.dto.ScoringStatsDto;
import io.renthell.scoringmgmtsrv.service.ScoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ScoringMgmtController {

    @Autowired
    private ScoringService scoringService;

    @RequestMapping(value = "/scoring-stats", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<ScoringStatsDto>> getScoringStatsWithRequestParams(
            @RequestParam(value="aggregate", defaultValue = "false", required = false) Boolean aggregate,
            @RequestParam(value="transactionId", defaultValue = "3", required = false) String transactionId,
            @RequestParam(value="month", required = false) Integer month,
            @RequestParam(value="year", required = false) Integer year,
            @RequestParam(value="postalCode", required = false) String postalCode
            ) {
        log.info("Get all Scoring Stats");

        List<ScoringStatsDto> stats = scoringService.find(aggregate, transactionId, year, month, postalCode);

        return new ResponseEntity<>((Collection<ScoringStatsDto>) stats, HttpStatus.OK);
    }

}
