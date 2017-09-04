package io.renthell.alertmgmtsrv.web.controller;

import io.renthell.alertmgmtsrv.service.AlertService;
import io.renthell.alertmgmtsrv.web.dto.AlertDto;
import io.renthell.alertmgmtsrv.web.exception.AlertNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
@Slf4j
public class AlertMgmtController {

    @Autowired
    private AlertService alertService;

    @RequestMapping(value = "/alert", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<AlertDto>> getAllAlerts() {
        log.info("Get all Alerts");

        return new ResponseEntity<>(alertService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/alert/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<AlertDto> getAlertWithId(@PathVariable String id) {
        AlertDto alertDto = alertService.findOne(id);
        if(alertDto != null) {
            return new ResponseEntity<>(alertDto, HttpStatus.OK);
        }

        throw new AlertNotFoundException(id);
    }

}
