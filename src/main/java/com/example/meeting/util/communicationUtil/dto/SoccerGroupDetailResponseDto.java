package com.example.meeting.util.communicationUtil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SoccerGroupDetailResponseDto {
    private final Long group_id;

    private final Long owner_id;

    private final List<Long> members;

    private final String group_name;

    private final Long group_member_count;

    private final String group_profile_pict_path;

    private final String group_description;

    private final String group_region;
}
