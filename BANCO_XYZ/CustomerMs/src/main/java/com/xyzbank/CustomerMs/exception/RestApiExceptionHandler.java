package com.xyzbank.CustomerMs.exception;

import com.xyzbank.CustomerMs.model.ResponseModel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseModel> handleIllegarArgumentException(IllegalArgumentException ex) {
        ResponseModel response = new ResponseModel();

        response.setCode(400);
        response.setType("BAD REQUEST");
        response.setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseModel> handleEntityNotFoundException(EntityNotFoundException ex) {
        ResponseModel response = new ResponseModel();

        response.setCode(404);
        response.setType("NOT FOUND");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseModel> handleIllegalStateException(IllegalStateException ex) {
        ResponseModel response = new ResponseModel();

        response.setCode(409);
        response.setType("CONFLICT");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseModel> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ResponseModel response = new ResponseModel();

        response.setCode(409);
        response.setType("CONFLICT");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel> handleGenericException(Exception ex) {
        ResponseModel response = new ResponseModel();

        response.setCode(500);
        response.setType("INTERNAL SERVER ERROR");
        response.setMessage("Unexpected error: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
