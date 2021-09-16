package com.agleveratto.bitcoin.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * Exception Handler to manipulate custom exceptions
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * In case of a request to repository return empty or null value, then build this exception
     *
     * @param exception ResourceNotFoundException
     * @return a ErrorResponse build with a correct error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse onResourceFound(ResourceNotFoundException exception) {
        log.error("No resource found exception occurred: {} ", exception.getMessage());

        /* Build a Error Response object */
        ErrorResponse response = new ErrorResponse();
        response.getErrors().add(
                new Error(
                        HttpStatus.NOT_FOUND.value(),
                        "Resource not found",
                        exception.getMessage()));

        return response;
    }

}