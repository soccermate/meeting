package com.example.meeting.repository;

import com.example.meeting.repository.entity.planJoinMeeting.PlanJoinMeeting;
import com.example.meeting.repository.entity.planJoinMeeting.PlanJoinMeetingPk;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface PlanJoinMeetingRepository extends ReactiveCrudRepository<PlanJoinMeeting, PlanJoinMeetingPk>
{
    @Modifying
    @Transactional
    @Query("INSERT INTO plan_join_meeting(meeting_id, user_id) " +
            "VALUES(:#{#planJoinMeeting.meetingId}, :#{#planJoinMeeting.userId})" +
            "on conflict (meeting_id, user_id) do update set user_id = :#{#planJoinMeeting.userId};"
            )
    Mono<Integer> save(PlanJoinMeeting planJoinMeeting);

    @Modifying
    @Transactional
    @Query("DELETE FROM plan_join_meeting WHERE meeting_id = :meetingId AND user_id = :userId;")
    Mono<Integer> delete(@Param("meetingId") Long meetingId, @Param("userId") Long userId);
}
