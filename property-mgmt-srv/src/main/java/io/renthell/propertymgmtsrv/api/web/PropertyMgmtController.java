package io.renthell.propertymgmtsrv.api.web;

import io.renthell.propertymgmtsrv.api.model.PropertyTransactionApi;
import io.renthell.propertymgmtsrv.api.util.CustomErrorType;
import io.renthell.propertymgmtsrv.api.service.impl.EventStoreServiceDefault;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/property-transaction", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> createPropertyTransaction(final @Valid @RequestBody PropertyTransactionApi property) {
        log.info("Creating PropertyTransactionApi : {}", property);

        Boolean success = eventStoreService.addPropertyTransaction(property);

        if(success) {
            log.info("PropertyTransactionApi transaction added: OK");
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else {
            log.error("Unable to add property transaction {}" + property.getIdentifier());
            return new ResponseEntity<>(new CustomErrorType("Unable to add property transaction " +
                    property.getIdentifier()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
