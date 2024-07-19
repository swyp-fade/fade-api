package com.fade.global.exception;

import com.fade.global.dto.response.Response;
import com.fade.vote.exception.DuplicateVoteException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        Response<?> errorResponse = Response.error(status.value(), errorMessage, ex.getClass().getSimpleName());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public Response<?> handleApplicationException(ApplicationException e) {
        return Response.error(
                e.getErrorCode().getHttpStatus().value(),
                e.getMessage(),
                Map.of(
                        "errorCode", e.getErrorCode().name(),
                        "data", e.getData()
                )
        );
    }
}
