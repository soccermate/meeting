package com.example.meeting.controller;

import com.example.meeting.controller.dto.createMeetingDto.CreateMeetingDto;
import com.example.meeting.controller.dto.getMeetingDetailDto.GetMeetingDetailResponseDto;
import com.example.meeting.controller.dto.getMeetingsDto.GetMeetingsDto;
import com.example.meeting.controller.dto.getMeetingsDto.MeetingDate;
import com.example.meeting.controller.dto.getMeetingsDto.MeetingTime;
import com.example.meeting.controller.dto.submitVoteDto.SubmitVoteRequestDto;
import com.example.meeting.controller.util.ObjectConverter;
import com.example.meeting.controller.util.VerifyTokenResult;
import com.example.meeting.exception.GroupIdAndMeetingIdNotMatchingException;
import com.example.meeting.exception.ResourceNotFoundException;
import com.example.meeting.exception.UnAuthorizedException;
import com.example.meeting.service.MeetingService;
import com.example.meeting.service.dto.SaveMeetingServiceDto;
import com.example.meeting.service.dto.SubmitVoteServiceDto;
import com.example.meeting.util.communicationUtil.SoccerGroupFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.meeting.config.GlobalStaticVariables.AUTH_CREDENTIALS;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("soccer-group")
public class MeetingController
{
    private final MeetingService meetingService;

    private final SoccerGroupFeignClient soccerGroupFeignClient;


    @GetMapping("{groupId}/meetings")
    Mono<GetMeetingsDto> getMeetings(
            @RequestHeader(AUTH_CREDENTIALS) String authStr,
            @PathVariable Long groupId,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    )
    {
        Pageable pageable = PageRequest.of(page, size);

        return meetingService.getMeetingsByGroupId(groupId, pageable )
                .collectList()
                .map(meetings -> {
                    log.info(meetings.toString());

                    return new GetMeetingsDto(meetings);
                });
    }

    @PostMapping("{groupId}/meetings")
    Mono<Void> createMeetings(
            @RequestHeader(AUTH_CREDENTIALS) String authStr,
            @PathVariable Long groupId,
            @Valid @RequestBody CreateMeetingDto createMeetingDto
            )
    {

        VerifyTokenResult verifyTokenResult = ObjectConverter.convertAuthCredentials(authStr);

        return soccerGroupFeignClient.getGroup(authStr, groupId)
                .switchIfEmpty(Mono.defer(()->{
                    throw new ResourceNotFoundException("group " + groupId + " is not found!");
                }))
                .flatMap(soccerGroupDetailResponseDto -> {

                    if(!soccerGroupDetailResponseDto.getOwner_id().equals(verifyTokenResult.getUser_id()))
                    {
                        throw new UnAuthorizedException("the user " + verifyTokenResult.getUser_id() + " is not authorized to create meeting!");
                    }

                    MeetingDate meetingDate = createMeetingDto.getMeeting_date();
                    MeetingTime meetingTime = createMeetingDto.getMeeting_time();

                    SaveMeetingServiceDto saveMeetingServiceDto = SaveMeetingServiceDto
                            .builder()
                            .groupId(groupId)
                            .meetingDate(LocalDate.of(meetingDate.getYear(), meetingDate.getMonth(), meetingDate.getDay()))
                            .meetingTime(LocalTime.of(meetingTime.getHour(), meetingTime.getMinute()))
                            .meetingCategory(createMeetingDto.getMeeting_category())
                            .meetingLocation(createMeetingDto.getMeeting_location())
                            .meetingYCoordinate(createMeetingDto.getMeeting_y_coordinate())
                            .meetingXCoordinate(createMeetingDto.getMeeting_x_coordinate())
                            .meetingName(createMeetingDto.getMeeting_name())
                            .build();

                    return meetingService.saveMeeting(saveMeetingServiceDto);
                })
                .flatMap(meeting -> {
                    return Mono.empty();
                });
    }

    @GetMapping("{groupId}/meetings/{meetingId}")
    Mono<GetMeetingDetailResponseDto> getMeetingDetail(
            @RequestHeader(AUTH_CREDENTIALS) String authStr,
            @PathVariable Long groupId,
            @PathVariable Long meetingId
    )
    {
        return meetingService.getMeetingDetail(meetingId)
                .map(getMeetingDetailServiceDto -> {

                    if(!getMeetingDetailServiceDto.getGroupId().equals(groupId))
                    {
                        throw new GroupIdAndMeetingIdNotMatchingException("meeting " + meetingId + " is not in group " + groupId);
                    }

                    LocalDate meetingDate = getMeetingDetailServiceDto.getMeetingDate();
                    LocalTime meetingTime = getMeetingDetailServiceDto.getMeetingTime();

                    return GetMeetingDetailResponseDto
                            .builder()
                            .meeting_id(getMeetingDetailServiceDto.getMeetingId())
                            .meeting_category(getMeetingDetailServiceDto.getMeetingCategory())
                            .meeting_date(new MeetingDate(meetingDate))
                            .meeting_time(new MeetingTime(meetingTime))
                            .meeting_y_coordinate(getMeetingDetailServiceDto.getMeetingYCoordinate())
                            .meeting_x_coordinate(getMeetingDetailServiceDto.getMeetingXCoordinate())
                            .meeting_location(getMeetingDetailServiceDto.getMeetingLocation())
                            .group_id(getMeetingDetailServiceDto.getGroupId())
                            .joined_members(getMeetingDetailServiceDto.getJoinedMembers())
                            .meeting_name(getMeetingDetailServiceDto.getMeetingName())
                            .build();
                });
    }


    @PostMapping("{groupId}/meetings/{meetingId}/join")
    Mono<Void> submitVote(
            @RequestHeader(AUTH_CREDENTIALS) String authStr,
            @PathVariable Long groupId,
            @PathVariable Long meetingId,
            @Valid @RequestBody SubmitVoteRequestDto submitVoteRequestDto
            )
    {
        VerifyTokenResult verifyTokenResult = ObjectConverter.convertAuthCredentials(authStr);

        return soccerGroupFeignClient.getGroup(authStr,groupId)
                .flatMap(soccerGroupDetailResponseDto -> {

                    log.info(soccerGroupDetailResponseDto.toString());

                    if(!soccerGroupDetailResponseDto.getMembers().contains(verifyTokenResult.getUser_id()))
                    {
                        throw new UnAuthorizedException("user " + verifyTokenResult.getUser_id() + " is not in group " +groupId );
                    }

                    SubmitVoteServiceDto submitVoteServiceDto = SubmitVoteServiceDto.builder()
                            .meetingId(meetingId)
                            .yCoordinate(submitVoteRequestDto.getY_coordinate())
                            .xCoordinate(submitVoteRequestDto.getX_coordinate())
                            .userId(verifyTokenResult.getUser_id())
                            .build();

                    return meetingService.submitVote(submitVoteServiceDto);

                })
                .onErrorResume(throwable -> {
                    if(throwable instanceof FeignException.FeignClientException.NotFound)
                    {
                        return Mono.error(new ResourceNotFoundException("soccer group " + groupId + " does not exist!"));
                    }

                    return Mono.error(throwable);
                })
                .flatMap(result -> {
                    return Mono.empty();
                });
    }

    @PostMapping("{groupId}/meetings/{meetingId}/attendance")
    Mono<Void> createAttendance(
            @RequestHeader(AUTH_CREDENTIALS) String authStr,
            @PathVariable Long groupId,
            @PathVariable Long meetingId
    )
    {
        VerifyTokenResult verifyTokenResult = ObjectConverter.convertAuthCredentials(authStr);

        return soccerGroupFeignClient.getGroup(authStr,groupId)
                .flatMap(soccerGroupDetailResponseDto -> {

                    log.info(soccerGroupDetailResponseDto.toString());

                    if(!soccerGroupDetailResponseDto.getMembers().contains(verifyTokenResult.getUser_id()))
                    {
                        throw new UnAuthorizedException("user " + verifyTokenResult.getUser_id() + " is not in group " +groupId );
                    }

                    return meetingService.createAttendance(meetingId, verifyTokenResult.getUser_id());

                })
                .onErrorResume(throwable -> {
                    if(throwable instanceof FeignException.FeignClientException.NotFound)
                    {
                        return Mono.error(new ResourceNotFoundException("soccer group " + groupId + " does not exist!"));
                    }

                    return Mono.error(throwable);
                })
                .flatMap(result -> {
                    return Mono.empty();
                });
    }

    @DeleteMapping("{groupId}/meetings/{meetingId}/attendance")
    Mono<Void> deleteAttendance(
            @RequestHeader(AUTH_CREDENTIALS) String authStr,
            @PathVariable Long groupId,
            @PathVariable Long meetingId
    )
    {
        VerifyTokenResult verifyTokenResult = ObjectConverter.convertAuthCredentials(authStr);

        return meetingService.deleteAttendance(meetingId,groupId)
                .flatMap(result -> {
                    return Mono.empty();
                });
    }



}
