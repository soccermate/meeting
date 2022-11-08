package com.example.meeting.repository.entity.joinedMeeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name="joined_meeting")
public class JoinedMeeting
{

    @Column("meeting_id")
    private Long meetingId;

    @Column("user_id")
    private Long userId;

    @Column("joined_time")
    private LocalDateTime joinedTime;

    @Column("earned_points")
    private Integer earnedPoint;

    @Column("user_x_coordinate")
    private Double userXCoordinate;

    @Column("user_y_coordinate")
    private Double userYCoordinate;

}
