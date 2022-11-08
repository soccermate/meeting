package com.example.meeting.repository.entity.planJoinMeeting;

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
@Table(name="plan_join_meeting")
public class PlanJoinMeeting
{
    @Column("meeting_id")
    private Long meetingId;

    @Column("user_id")
    private Long userId;
}
