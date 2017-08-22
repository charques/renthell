package io.renthell.propertymgmtsrv.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class PropertyMgmtErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
        String error;
        for (FieldError fieldError : fieldErrors) {
            error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
            errors.add(error);
        }
        for (ObjectError objectError : globalErrors) {
            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
            errors.add(error);
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(status);
        responseBuilder.headers(headers);
        return responseBuilder.body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add("Unsupported content type: " + ex.getContentType());
        errors.add("Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes()));

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(status);
        responseBuilder.headers(headers);
        return responseBuilder.body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        List<String> errors = new ArrayList<>();
        if (mostSpecificCause != null) {
            String exceptionName = mostSpecificCause.getClass().getName();
            String message = mostSpecificCause.getMessage();
            errors.add(exceptionName + ": " + message);
        } else {
            errors.add(ex.getMessage());
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(status);
        responseBuilder.headers(headers);
        return responseBuilder.body(errors);
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<Object> handleEventNotFoundException(HttpServletRequest req, PropertyNotFoundException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", ex.getId());
        map.put("reason", "Property can not be found");
        map.put("url", req.getRequestURI());

        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.NOT_FOUND);
        return responseBudiler.body(map);
    }

    @ExceptionHandler(EventProcesingException.class)
    public ResponseEntity<Object> handleEventProcesingException(HttpServletRequest req, EventProcesingException ex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", ex.getThrowable().getLocalizedMessage());
        map.put("reason", "Event can not be procesed");
        map.put("url", req.getRequestURI());

        ResponseEntity.BodyBuilder responseBudiler = ResponseEntity.status(HttpStatus.BAD_REQUEST);
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