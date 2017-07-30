package io.renthell.propertymgmtsrv.web;

import io.renthell.propertymgmtsrv.model.Property;
import io.renthell.propertymgmtsrv.service.impl.EventStoreServiceDefault;
import io.renthell.propertymgmtsrv.util.CustomErrorType;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
public class PropertyMgmtController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventStoreServiceDefault eventStoreService;

    @RequestMapping(value = "/property", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> createPropertyTransaction(final @Valid @RequestBody Property property) {
        log.info("Creating Property : {}", property);

        Boolean success = eventStoreService.addPropertyTransaction(property);

        if(success) {
            log.info("Property transaction added: OK");
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else {
            log.error("Unable to add property transaction {}" + property.getIdentifier());
            return new ResponseEntity<>(new CustomErrorType("Unable to add property transaction " +
                    property.getIdentifier()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
