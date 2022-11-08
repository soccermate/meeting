package com.example.meeting.controller.dto.createMeetingDto;

import com.example.meeting.controller.dto.getMeetingsDto.MeetingDate;
import com.example.meeting.controller.dto.getMeetingsDto.MeetingTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CreateMeetingDto
{
    @NotBlank(message = "meeting_name should not be blank!")
    @Size(min = 3, message = "meeting_name should be at least 3 characters long")
    private final String meeting_name;


    private final MeetingDate meeting_date;

    @Min(-90)
    @Max(90)
    private final Double meeting_x_coordinate;

    @Min(-180)
    @Max(180)
    private final Double meeting_y_coordinate;

    @NotBlank
    private final String meeting_location;

    private final MeetingTime meeting_time;

    @NotBlank
    private final String meeting_category;
}
