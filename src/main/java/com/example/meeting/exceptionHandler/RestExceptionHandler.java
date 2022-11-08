package com.example.meeting.exceptionHandler;


import com.example.meeting.exception.DistanceNotCloseException;
import com.example.meeting.exception.GroupIdAndMeetingIdNotMatchingException;
import com.example.meeting.exception.ResourceNotFoundException;
import com.example.meeting.exception.UserAlreadyJoinedException;
import com.example.meeting.exceptionHandler.dto.ErrorMsgDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(DistanceNotCloseException.class)
    ResponseEntity DistanceNotCloseException(DistanceNotCloseException ex) {
        log.debug("handling exception::" + ex);

        return new ResponseEntity(new ErrorMsgDto(new Date(), "distance should be within 10m!", ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity ResourceNotFoundException(ResourceNotFoundException ex) {
        log.debug("handling exception::" + ex);

        return new ResponseEntity(new ErrorMsgDto(new Date(), "the requested resource is not found!", ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GroupIdAndMeetingIdNotMatchingException.class)
    ResponseEntity GroupIdAndMeetingIdNotMatchingException(GroupIdAndMeetingIdNotMatchingException ex) {
        log.debug("handling exception::" + ex);

        return new ResponseEntity(new ErrorMsgDto(new Date(), "group id and meeting id is not matching! ", ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserAlreadyJoinedException.class)
    ResponseEntity UserAlreadyJoinedException(UserAlreadyJoinedException ex) {
        log.debug("handling exception::" + ex);

        return new ResponseEntity(new ErrorMsgDto(new Date(), "user already joined the meeting before!", ex.getMessage()),
                HttpStatus.CONFLICT);
    }


    @ExceptionHandler({WebExchangeBindException.class})
    ResponseEntity webExchangeBindException(WebExchangeBindException ex)
    {
        String rejectedValue = ex.getBindingResult().getFieldError().getRejectedValue() == null? ""
                : ex.getBindingResult().getFieldError().getRejectedValue().toString();
        String message = ex.getBindingResult().getFieldError().getDefaultMessage() == null? ""
                :ex.getBindingResult().getFieldError().getDefaultMessage();

        return new ResponseEntity(
                new ErrorMsgDto(new Date(), "bad request ", rejectedValue
                        + " is not valid with message : "
                        + message),
                HttpStatus.BAD_REQUEST
        );
    }
}
