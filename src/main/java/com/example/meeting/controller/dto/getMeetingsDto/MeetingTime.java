package com.example.meeting.controller.dto.getMeetingsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MeetingTime
{
    private final Integer hour;

    private final Integer minute;

}
