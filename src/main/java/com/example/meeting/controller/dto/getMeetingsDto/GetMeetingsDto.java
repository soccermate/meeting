package com.example.meeting.controller.dto.getMeetingsDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class GetMeetingsDto
{
    private final List<Meeting> meetings;

    public GetMeetingsDto(List<com.example.meeting.repository.entity.Meeting> meetings)
    {
        this.meetings = new ArrayList<>();

        for(com.example.meeting.repository.entity.Meeting meeting: meetings)
        {
            this.meetings.add(new Meeting(meeting));
        }
    }
}
