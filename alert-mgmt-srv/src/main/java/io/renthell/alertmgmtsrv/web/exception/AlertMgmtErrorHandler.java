package io.renthell.alertmgmtsrv.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AlertMgmtErrorHandler {

    @ExceptionHandler(AlertNotFoundException.class)
    public ResponseEntity<Object> handleEventNotFoundException(HttpServletRequest req, AlertNotFoundException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", ex.getId());
        map.put("reason", "Alert can not be found");
        map.put("url", req.getRequestURI());

        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.NOT_FOUND);
        return responseBudiler.body(map);
    }
}