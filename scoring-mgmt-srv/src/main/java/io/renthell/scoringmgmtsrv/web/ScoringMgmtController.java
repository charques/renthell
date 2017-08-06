package io.renthell.scoringmgmtsrv.web;

import io.renthell.scoringmgmtsrv.model.Scoring;
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

    @RequestMapping(value = "/scoring", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<Scoring>> getAllProperties() {
        log.info("Get all Properties");

        return new ResponseEntity<>((Collection<Scoring>) scoringService.findAll(), HttpStatus.OK);
    }

}
