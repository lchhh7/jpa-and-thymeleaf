package com.jinjin.jintranet.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    //400
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> BadRequestException(final RuntimeException e) {
        log.warn("error" , e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    //401
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException  e) {
        log.warn("error" , e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    //404
    @ExceptionHandler(NoHandlerFoundException.class)
    public String noHandlerException(Model model , Exception e) {
        model.addAttribute("errorMessage" , e);
        return "include/errorPage";
    }
    //500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(final Exception e) {
        log.warn("error" , e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }





    @ExceptionHandler(Exception.class)
    public ResponseEntity HandlerException(Exception e) {
        return new ResponseEntity("처리 중 오류가 발생했습니다. " + e.getMessage() ,HttpStatus.CONFLICT);
    }
}
