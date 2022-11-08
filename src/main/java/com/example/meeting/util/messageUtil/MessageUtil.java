package com.example.meeting.util.messageUtil;

import com.example.meeting.util.messageUtil.dto.PointEarnedMessage;
import reactor.core.publisher.Mono;

public interface MessageUtil {

    public Mono<Boolean> sendPointEarnedMessage(PointEarnedMessage pointEarnedMessage);
}
