package com.example.meeting.exception;

public class UserAlreadyJoinedException extends RuntimeException
{
    public UserAlreadyJoinedException(String msg)
    {
        super(msg);
    }
}
