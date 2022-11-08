package com.example.meeting.repository;

import com.example.meeting.repository.entity.joinedMeeting.JoinedMeeting;
import com.example.meeting.repository.entity.joinedMeeting.JoinedMeetingPrimaryKey;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface JoinedMeetingRepository extends ReactiveCrudRepository<JoinedMeeting, JoinedMeetingPrimaryKey>
{
        Flux<JoinedMeeting> findByMeetingId(Long meetingId);

        @Modifying
        @Transactional
        @Query("insert into joined_meeting(meeting_id, user_id, earned_points, user_x_coordinate, user_y_coordinate) " +
                "values(:#{#joinedMeeting.meetingId}, :#{#joinedMeeting.userId}, :#{#joinedMeeting.earnedPoint}, " +
                ":#{#joinedMeeting.userXCoordinate}, :#{#joinedMeeting.userYCoordinate})")
        Mono<Integer> save(JoinedMeeting joinedMeeting);

}
