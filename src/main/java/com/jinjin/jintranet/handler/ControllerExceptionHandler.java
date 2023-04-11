package com.jinjin.jintranet.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String noHandlerException(Model model , Exception e) {
        model.addAttribute("errorMessage" , e);
        return "include/errorPage";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity HandlerException(Exception e) {
        return new ResponseEntity("처리 중 오류가 발생했습니다. " + e.getMessage() ,HttpStatus.CONFLICT);
    }
}
