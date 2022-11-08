package com.example.meeting.util.messageUtil.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class PointEarnedMessage {

    private Long userId;

    private Long meetingId;

    private Long groupId;

    private double userXCoordinate;

    private double userYCoordinate;

    private int earnedPoint;

    private int earnedYear;

    private int earnedMonth;

    private int earnedDay;

    private int earnedHour;

    private int earnedMinute;

    private int earnedSeconds;

}
