package io.renthell.alertmgmtsrv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.renthell.alertmgmtsrv.web.dto.AlertDto;

public interface EventStoreService {

    public Boolean addAlertEvent(AlertDto item) throws JsonProcessingException;
}
