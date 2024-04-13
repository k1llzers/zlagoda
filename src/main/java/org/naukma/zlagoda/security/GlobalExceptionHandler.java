package org.naukma.zlagoda.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.zlagoda.exception.NoSuchEntityException;
import org.naukma.zlagoda.security.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.info(e.getMessage());
        return ResponseEntity.ok(new ErrorResponse("Incorrect username or password", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchEntityException(NoSuchEntityException e) {
        return ResponseEntity.ok(new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value()));
    }
}
