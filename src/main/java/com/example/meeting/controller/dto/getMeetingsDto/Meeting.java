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

    private final MeetingDate meeting_date;

    private final MeetingTime meeting_time;

    private final String meeting_location;

    private final String meeting_category;

    private final Long group_id;


    public Meeting(com.example.meeting.repository.entity.Meeting meeting)
    {
        this.meeting_id = meeting.getMeetingId();

        this.meeting_name = meeting.getMeetingName();

        this.meeting_date = new MeetingDate(meeting.getMeetingDate().getYear(),
                meeting.getMeetingDate().getMonthValue(),
                meeting.getMeetingDate().getDayOfMonth());

        this.meeting_time = new MeetingTime(
                meeting.getMeetingTime().getHour(),
                meeting.getMeetingTime().getMinute()
        );

        this.meeting_location = meeting.getMeetingLocation();

        this.meeting_category = meeting.getMeetingCategory();

        this.group_id = meeting.getGroupId();
    }
}
