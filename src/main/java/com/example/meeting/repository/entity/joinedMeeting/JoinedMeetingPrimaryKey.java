package com.example.meeting.repository.entity.joinedMeeting;

import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;

public class JoinedMeetingPrimaryKey implements Serializable {

    private Long meetingId;

    private Long userId;
}
