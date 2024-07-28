package com.fade.global.exception;

import com.fade.global.constant.ErrorCode;
import com.fade.global.dto.response.Response;
import com.fade.vote.exception.DuplicateVoteException;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Collections;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError.getDefaultMessage();

        Response<?> errorResponse = Response.error(status.value(), errorMessage, ex.getClass().getSimpleName());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Response<?>> handleApplicationException(ApplicationException e) {

        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(Response.error(
                        e.getErrorCode().getHttpStatus().value(),
                        e.getMessage(),
                        Map.of(
                                "errorCode", e.getErrorCode().name(),
                                "data", e.getData()
                        )
                ));
    }

    @ExceptionHandler(Exception.class)
    public Response<?> handleException(Exception e) {
        this.logger.error(e.getMessage(), e);

        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return Response.error(
                errorCode.getHttpStatus().value(),
                e.getMessage(),
                Map.of(
                        "errorCode", errorCode.name(),
                        "data", Collections.emptyMap()
                )
        );
    }
}
