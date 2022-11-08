package com.example.meeting.service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class SaveMeetingServiceDto
{
    private String meetingName;

    private LocalDate meetingDate;

    private double meetingXCoordinate;

    private double meetingYCoordinate;

    private String meetingLocation;

    private LocalTime meetingTime;

    private String meetingCategory;

    private Long groupId;

}
