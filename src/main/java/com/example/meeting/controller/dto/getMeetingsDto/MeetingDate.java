package com.example.meeting.controller.dto.getMeetingsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MeetingDate {

    private final Integer year;

    private final Integer month;

    private final Integer day;

}
