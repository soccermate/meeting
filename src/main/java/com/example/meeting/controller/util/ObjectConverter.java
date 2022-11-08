package com.example.meeting.controller.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class ObjectConverter
{
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static VerifyTokenResult convertAuthCredentials(String authCredentialsJsonStr)
    {
        return objectMapper.readValue(authCredentialsJsonStr, VerifyTokenResult.class);
    }
}