package com.example.meeting.exception;

public class DistanceNotCloseException extends RuntimeException
{
    public DistanceNotCloseException(String msg)
    {
        super(msg);
    }
}
