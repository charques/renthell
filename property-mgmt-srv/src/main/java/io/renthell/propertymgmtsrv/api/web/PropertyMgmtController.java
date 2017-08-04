package io.renthell.propertymgmtsrv.api.web;

import io.renthell.propertymgmtsrv.api.dto.PropertyTransactionDto;
import io.renthell.propertymgmtsrv.api.exception.PropertyMgmtException;
import io.renthell.propertymgmtsrv.api.model.Property;
import io.renthell.propertymgmtsrv.api.service.impl.PropertyServiceDefault;
import io.renthell.propertymgmtsrv.api.util.CustomErrorType;
import io.renthell.propertymgmtsrv.api.service.impl.EventStoreServiceDefault;
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
    private EventStoreServiceDefault eventStoreService;

    @Autowired
    private PropertyServiceDefault propertyService;

    @RequestMapping(value = "/property-transaction", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> createPropertyTransaction(final @Valid @RequestBody PropertyTransactionDto property) {
        log.info("Creating PropertyTransactionDto : {}", property);

        try {
            eventStoreService.addPropertyTransaction(property);
        } catch (PropertyMgmtException e) {
            log.error("Unable to add property transaction {}: {}", property.getIdentifier(), e.getErrorCode().getValue());

            return new ResponseEntity<>(new CustomErrorType("Unable to add property transaction " +
                    property.getIdentifier(), e.getErrorCode().getValue()), HttpStatus.BAD_REQUEST);
        }

        log.info("Property transaction added: OK");
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/property-transaction", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Collection<Property>> getAllProperties() {
        log.info("Get all Properties");

        return new ResponseEntity<>((Collection<Property>) propertyService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/property-transaction/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<Property> getPropertyWithId(@PathVariable String id) {
        return new ResponseEntity<>(propertyService.findOne(id),HttpStatus.OK);
    }
}
