package com.example.meeting.exception;

public class UnAuthorizedException extends  RuntimeException{
    public UnAuthorizedException(String msg)
    {
        super(msg);
    }
}
