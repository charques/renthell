package io.renthell.eventstoresrv.web.exception;

import io.renthell.eventstoresrv.service.exception.EventRetrievingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class EventStoreErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Object> handleEventNotFoundException(HttpServletRequest req, EventNotFoundException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", ex.getId());
        map.put("reason", "Event can not be found");
        map.put("url", req.getRequestURI());
        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.NOT_FOUND);
        ResponseEntity<Object> response = responseBudiler.body(map);
        return response;
    }

    @ExceptionHandler(EventRetrievingException.class)
    public ResponseEntity<Object> handleEventRetrievingException(HttpServletRequest req, EventRetrievingException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", ex.getId());
        map.put("reason", "Event can not be retrieved");
        map.put("url", req.getRequestURI());
        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        ResponseEntity<Object> response = responseBudiler.body(map);
        return response;
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(HttpServletRequest req, BadRequestException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("reason", "Event can not be created");
        map.put("url", req.getRequestURI());
        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        ResponseEntity<Object> response = responseBudiler.body(map);
        return response;
    }
}