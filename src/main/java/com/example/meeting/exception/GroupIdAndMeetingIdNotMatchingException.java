package com.example.meeting.exception;

public class GroupIdAndMeetingIdNotMatchingException extends RuntimeException
{
    public GroupIdAndMeetingIdNotMatchingException(String msg)
    {
        super(msg);
    }
}
