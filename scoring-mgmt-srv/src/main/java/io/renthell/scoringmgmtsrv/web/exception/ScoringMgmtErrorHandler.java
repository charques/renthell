package io.renthell.scoringmgmtsrv.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ScoringMgmtErrorHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(EventProcesingException.class)
    public ResponseEntity<Object> handleEventProcesingException(HttpServletRequest req, EventProcesingException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", ex.getThrowable().getLocalizedMessage());
        map.put("reason", "Event can not be procesed");
        map.put("url", req.getRequestURI());
        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        ResponseEntity<Object> response = responseBudiler.body(map);
        return response;
    }
}