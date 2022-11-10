package com.example.meeting.controller.dto.getMeetingsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MeetingTime
{
    private final Integer hour;

    private final Integer minute;

    public MeetingTime(LocalTime localTime)
    {
        this.hour = localTime.getHour();

        this.minute = localTime.getMinute();

    }

}
