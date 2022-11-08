package com.example.meeting.controller.dto.submitVoteDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor(force = true)
public class SubmitVoteRequestDto
{
    @Min(-90)
    @Max(90)
    private final Double x_coordinate;

    @Min(-180)
    @Max(180)
    private final Double y_coordinate;
}
