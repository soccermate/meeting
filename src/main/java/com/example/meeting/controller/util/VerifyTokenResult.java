package com.example.meeting.controller.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class VerifyTokenResult {

    private final long user_id;

    private final String role;

    private final boolean valid;
}