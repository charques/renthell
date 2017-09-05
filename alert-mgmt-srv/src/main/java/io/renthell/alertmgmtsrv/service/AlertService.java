package io.renthell.alertmgmtsrv.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.alertmgmtsrv.web.dto.AlertDto;
import io.renthell.alertmgmtsrv.web.dto.PropertyTransactionDto;

import java.util.List;


public interface AlertService {

    void evaluateProperty(PropertyTransactionDto propertyDto) throws JsonProcessingException;

    List<AlertDto> findAll();

    AlertDto findOne(String id);

}
