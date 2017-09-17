package io.renthell.propertymgmtsrv.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.propertymgmtsrv.web.dto.PropertyDto;
import io.renthell.propertymgmtsrv.service.EventStoreService;
import io.renthell.propertymgmtsrv.service.PropertyService;
import io.renthell.propertymgmtsrv.web.dto.PropertyInputDto;
import io.renthell.propertymgmtsrv.web.exception.BadRequestException;
import io.renthell.propertymgmtsrv.web.exception.PropertyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api")
@Slf4j
public class PropertyMgmtController {

    @Autowired
    private EventStoreService eventStoreService;

    @Autowired
    private PropertyService propertyService;

    @RequestMapping(value = "/property-transaction", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> createPropertyTransaction(final @Valid @RequestBody PropertyInputDto property) {

        try {
            eventStoreService.produceAddPropertyTransactionEvent(property);
            log.info("createPropertyTransaction: OK");
            return new ResponseEntity<String>(HttpStatus.OK);

        } catch (JsonProcessingException e) {
            log.error("Unable to add property transaction {}: {}", property.getIdentifier(), e.getMessage());
            throw new BadRequestException(e);
        }
    }

    @RequestMapping(value = "/property-transaction", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<PropertyDto>> getAllProperties() {
        log.info("Get all Properties");

        return new ResponseEntity<>((Collection<PropertyDto>) propertyService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/property-transaction/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<PropertyDto> getPropertyWithId(@PathVariable String id) {
        PropertyDto propertyDto = propertyService.findOne(id);
        if(propertyDto != null) {
            return new ResponseEntity<>(propertyDto, HttpStatus.OK);
        }

        throw new PropertyNotFoundException(id);
    }
}
