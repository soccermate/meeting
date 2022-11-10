package com.example.meeting.controller.dto.getMeetingsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Meeting
{
    private final Long meeting_id;

    private final String meeting_name;

    private final String meeting_date;

    private final String meeting_time;

    private final String meeting_location;

    private final String meeting_category;

    private final Long group_id;

    private final String meeting_title;


    public Meeting(com.example.meeting.repository.entity.Meeting meeting)
    {
        this.meeting_title = meeting.getMeetingName();

        this.meeting_id = meeting.getMeetingId();

        this.meeting_name = meeting.getMeetingName();

        this.meeting_date = meeting.getMeetingDate().toString();

        this.meeting_time = meeting.getMeetingTime().toString();

        this.meeting_location = meeting.getMeetingLocation();

        this.meeting_category = meeting.getMeetingCategory();

        this.group_id = meeting.getGroupId();
    }
}
