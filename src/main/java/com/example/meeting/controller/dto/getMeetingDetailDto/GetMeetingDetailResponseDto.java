package com.example.meeting.controller.dto.getMeetingDetailDto;

import com.example.meeting.controller.dto.getMeetingsDto.MeetingDate;
import com.example.meeting.controller.dto.getMeetingsDto.MeetingTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class GetMeetingDetailResponseDto
{
    private final Long group_id;

    private final Long meeting_id;

    private final String meeting_name;

    private final String meeting_date;

    private final String meeting_time;

    private final String meeting_location;

    private final String meeting_category;

    private final Double meeting_x_coordinate;

    private final Double meeting_y_coordinate;

    private final List<Long> joined_members;
}
