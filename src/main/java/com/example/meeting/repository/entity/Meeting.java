package com.example.meeting.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name="meeting")
public class Meeting
{
    @Id
    @Column("meeting_id")
    private Long meetingId;

    @Column("group_id")
    private Long groupId;

    @Column("meeting_name")
    private String meetingName;

    @Column("meeting_date")
    private LocalDate meetingDate;

    @Column("meeting_time")
    private LocalTime meetingTime;

    @Column("meeting_location")
    private String meetingLocation;

    @Column("meeting_category")
    private String meetingCategory;

    @Column("x_coordinate")
    private Double xCoordinate;

    @Column("y_coordinate")
    private Double yCoordinate;

}
