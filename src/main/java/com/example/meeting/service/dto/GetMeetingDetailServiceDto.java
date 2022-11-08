package com.example.meeting.service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class GetMeetingDetailServiceDto
{
        private Long groupId;

        private Long meetingId;

        private String meetingName;

        private LocalDate meetingDate;

        private String meetingLocation;

        private LocalTime meetingTime;

        private String meetingCategory;

        private Double meetingXCoordinate;

        private Double meetingYCoordinate;

        private List<Long> joinedMembers;
}
