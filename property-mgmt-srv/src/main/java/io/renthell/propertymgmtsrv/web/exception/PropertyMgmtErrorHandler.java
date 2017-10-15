package io.renthell.propertymgmtsrv.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class PropertyMgmtErrorHandler {

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<Object> handleEventNotFoundException(HttpServletRequest req, PropertyNotFoundException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", ex.getId());
        map.put("reason", "Property can not be found");
        map.put("url", req.getRequestURI());

        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.NOT_FOUND);
        return responseBudiler.body(map);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(HttpServletRequest req, BadRequestException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", ex.getThrowable().getLocalizedMessage());
        map.put("reason", "Property can not be created");
        map.put("url", req.getRequestURI());

        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        return responseBudiler.body(map);
    }

    @ExceptionHandler(PostEventException.class)
    public ResponseEntity<Object> handlePostEventException(HttpServletRequest req, PostEventException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", ex.getErrorCode());
        map.put("message", ex.getMessage());
        map.put("reason", "Property can not be created");
        map.put("url", req.getRequestURI());

        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        return responseBudiler.body(map);
    }
}