package com.example.meeting.util.messageUtil;

import com.example.meeting.util.messageUtil.dto.PointEarnedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageUtil implements MessageUtil
{
    private final KafkaSender<Integer, PointEarnedMessage> pointEarnedMessageKafkaSender;

    @Value("${spring.kafka.template.default-topic}")
    private String POINT_EARNED_TOPIC;

    public Mono<Boolean> sendPointEarnedMessage( PointEarnedMessage pointEarnedMessage)
    {
        SenderRecord<Integer, PointEarnedMessage, Object> senderRecord = SenderRecord.create(POINT_EARNED_TOPIC, null, null, null, pointEarnedMessage, null );

        log.debug("right before sending message!");
       return pointEarnedMessageKafkaSender.send(Mono.just(senderRecord))
               .next()
               .map(result -> {
                   log.info("point earned message sent to kafka!");
                   log.info(result.toString());

                   return true;
               })
               .onErrorResume(throwable -> {

                   log.error(throwable.getMessage());
                   return Mono.just(false);
               });
    }
}
