package com.example.meeting.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class SubmitVoteServiceDto
{
    private final Long meetingId;

    private final Double xCoordinate;

    private final Double yCoordinate;

    private final Long userId;
}
