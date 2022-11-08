package com.example.meeting.util.communicationUtil;


import com.example.meeting.util.communicationUtil.config.SoccerGroupFeignClientConfig;
import com.example.meeting.util.communicationUtil.dto.SoccerGroupDetailResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import static com.example.meeting.config.GlobalStaticVariables.AUTH_CREDENTIALS;

@ReactiveFeignClient(name="${spring.reactive-feign.service-name.soccer-group}",
        url="${spring.reactive-feign.service-name.soccer-group.url}",
        configuration = SoccerGroupFeignClientConfig.class
)
public interface SoccerGroupFeignClient
{
    @GetMapping("/soccer-group/{groupId}")
    public Mono<SoccerGroupDetailResponseDto> getGroup(@RequestHeader(AUTH_CREDENTIALS) String authStr,
                                                      @PathVariable Long groupId);

}
