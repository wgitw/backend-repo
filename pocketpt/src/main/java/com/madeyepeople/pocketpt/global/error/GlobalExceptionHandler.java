package com.madeyepeople.pocketpt.global.error;


import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Controller에서 Request Body의 @Valid 유효성 검증 실패시 발생
     *
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INPUT_INVALID_VALUE, bindingResult);
        log.error(e.getMessage());
        return new ResponseEntity<>(response, ErrorCode.INPUT_INVALID_VALUE.getStatus());
    }

    /**
     * Controller에서 path variable 또는 Entity의 @Valid 유효성 검증 실패시 발생
     * @param e ConstraintViolationException
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e) {
        BindingResult bindingResult = extractErrorMessages(e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INPUT_INVALID_VALUE, bindingResult);
        log.error(e.getMessage());
        return new ResponseEntity<>(response, ErrorCode.INPUT_INVALID_VALUE.getStatus());
    }

    // 따로 만들었으나, 기존 handleExceptionInternal 메소드를 사용하도록 수정
//    @ExceptionHandler
//    public ResponseEntity<Object> business(BusinessException e, WebRequest request) {
//        ErrorCode errorCode = e.getErrorCode();
//        ErrorResponse errorResponse = ErrorResponse.of(errorCode, errorCode.getMessage(e));
//        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
//    }

    @ExceptionHandler
    public ResponseEntity<Object> business(BusinessException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace();
        return handleExceptionInternal(e, ErrorCode.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception e, ErrorCode errorCode, WebRequest request) {
        log.error(e.getMessage());
        return handleExceptionInternal(e, errorCode, errorCode.getStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
            Exception e, ErrorCode errorCode, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                e, ErrorResponse.of(errorCode, e), HttpHeaders.EMPTY, status, request);
    }

//    private ResponseEntity<Object> handleExceptionInternal(
//            Exception e, ErrorCode errorCode, HttpStatus status, WebRequest request) {
//        return super.handleExceptionInternal(
//                e, ErrorResponse.of(errorCode), HttpHeaders.EMPTY, status, request);
//    }

    private BindingResult extractErrorMessages(ConstraintViolationException e) {
        List<FieldError> fieldErrors = e.getConstraintViolations().stream()
                .map(constraintViolation -> new FieldError(
                        constraintViolation.getRootBeanClass().getSimpleName(),
                        constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()))
                .toList();
        BindingResult bindingResult = new BindException(new ErrorResponse(), "errorResponse");
        fieldErrors.forEach(bindingResult::addError);
        return bindingResult;
    }
}

