/**
 * Created by Zorro on 10/25 025.
 */
package com.hcloud.apm.analysis.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来处理Spring MVC的异常返回
 */
@EnableWebMvc
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<Object> handleControllerException(HttpServletRequest req, Throwable ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof APMException) {
            errorResponse.setDetail(((APMException) ex).getDetail());
            status = HttpStatus.OK;
        }
        return new ResponseEntity<Object>(errorResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> responseBody = new HashMap<>();
        responseBody.put("path", request.getContextPath());
        responseBody.put("message", "The URL you have reached is not in service at this time (404).");
        return new ResponseEntity<Object>(responseBody, HttpStatus.NOT_FOUND);
    }
}
