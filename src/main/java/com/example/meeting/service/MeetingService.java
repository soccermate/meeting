package com.example.meeting.service;

import com.example.meeting.exception.DistanceNotCloseException;
import com.example.meeting.exception.ResourceNotFoundException;
import com.example.meeting.exception.UserAlreadyJoinedException;
import com.example.meeting.repository.JoinedMeetingRepository;
import com.example.meeting.repository.MeetingRepository;
import com.example.meeting.repository.PlanJoinMeetingRepository;
import com.example.meeting.repository.entity.Meeting;
import com.example.meeting.repository.entity.joinedMeeting.JoinedMeeting;
import com.example.meeting.repository.entity.planJoinMeeting.PlanJoinMeeting;
import com.example.meeting.service.dto.GetMeetingDetailServiceDto;
import com.example.meeting.service.dto.SaveMeetingServiceDto;
import com.example.meeting.service.dto.SubmitVoteServiceDto;
import com.example.meeting.util.messageUtil.MessageUtil;
import com.example.meeting.util.messageUtil.dto.PointEarnedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingService
{
    private final MeetingRepository meetingRepository;

    private final JoinedMeetingRepository joinedMeetingRepository;

    private final PlanJoinMeetingRepository planJoinMeetingRepository;

    private final MessageUtil messageUtil;


    public Flux<Meeting> getMeetingsByGroupId(Long groupId, Pageable pageable)
    {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("meetingDate").descending().and(Sort.by("meetingTime")).descending());

        return meetingRepository.findByGroupId(groupId, sortedPageable);
    }

    @Transactional("r2dbcTransactionManager")
    public Mono<Meeting> saveMeeting(SaveMeetingServiceDto saveMeetingServiceDto)
    {

        Meeting meeting = Meeting.builder()
                .meetingDate(saveMeetingServiceDto.getMeetingDate())
                .meetingLocation(saveMeetingServiceDto.getMeetingLocation())
                .meetingCategory(saveMeetingServiceDto.getMeetingCategory())
                .meetingName(saveMeetingServiceDto.getMeetingName())
                .meetingTime(saveMeetingServiceDto.getMeetingTime())
                .groupId(saveMeetingServiceDto.getGroupId())
                .xCoordinate(saveMeetingServiceDto.getMeetingXCoordinate())
                .yCoordinate(saveMeetingServiceDto.getMeetingYCoordinate())
                .build();

        return meetingRepository.save(meeting);

    }

    public Mono<GetMeetingDetailServiceDto> getMeetingDetail(Long meetingId)
    {
        return meetingRepository.findById(meetingId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("meeting " + meetingId + " does not exist!")))
                .flatMap(meeting -> {

                    return joinedMeetingRepository.findByMeetingId(meetingId)
                            .collectList()
                            .map(joinedMeeting -> {
                                return GetMeetingDetailServiceDto.builder()
                                        .meetingName(meeting.getMeetingName())
                                        .groupId(meeting.getGroupId())
                                        .joinedMembers(joinedMeeting.stream().map(joinedMeeting1 -> joinedMeeting1.getUserId()).toList())
                                        .meetingLocation(meeting.getMeetingLocation())
                                        .meetingId(meeting.getMeetingId())
                                        .meetingCategory(meeting.getMeetingCategory())
                                        .meetingXCoordinate(meeting.getXCoordinate())
                                        .meetingYCoordinate(meeting.getYCoordinate())
                                        .meetingDate(meeting.getMeetingDate())
                                        .meetingTime(meeting.getMeetingTime())
                                        .build();
                            });
                });
    }

    @Transactional("r2dbcTransactionManager")
    public Mono<Boolean> submitVote(SubmitVoteServiceDto submitVoteServiceDto)
    {
        return meetingRepository.findById(submitVoteServiceDto.getMeetingId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("meeting " + submitVoteServiceDto.getMeetingId() + " does not exist!")))
                .flatMap(meeting -> {

                    log.info("started to find matching meeting!");

                    double meetingXCoord = meeting.getXCoordinate();
                    double meetingYCoord = meeting.getYCoordinate();

                    double distance = meters(
                            meetingXCoord,
                            meetingYCoord,
                            submitVoteServiceDto.getXCoordinate(),
                            submitVoteServiceDto.getYCoordinate()
                    );

                    if(distance <= 10)
                    {
                        log.info("distance is " + String.valueOf(distance));

                        JoinedMeeting joinedMeeting = JoinedMeeting.builder()
                                .meetingId(submitVoteServiceDto.getMeetingId())
                                .earnedPoint(100)
                                .joinedTime(LocalDateTime.now())
                                .userXCoordinate(submitVoteServiceDto.getXCoordinate())
                                .userYCoordinate(submitVoteServiceDto.getYCoordinate())
                                .userId(submitVoteServiceDto.getUserId())
                                .build();


                        return joinedMeetingRepository.save(joinedMeeting)
                                .flatMap(result -> {
                                    if(result == 1)
                                    {
                                        log.info("done saving joined request in database");

                                        LocalDateTime localDateTime = LocalDateTime.now();

                                        PointEarnedMessage pointEarnedMessage = PointEarnedMessage.builder()
                                                .groupId(meeting.getGroupId())
                                                .userId(submitVoteServiceDto.getUserId())
                                                .earnedDay(localDateTime.getDayOfMonth())
                                                .earnedMonth(localDateTime.getMonthValue())
                                                .earnedYear(localDateTime.getYear())
                                                .earnedHour(localDateTime.getHour())
                                                .earnedMinute(localDateTime.getMinute())
                                                .meetingId(submitVoteServiceDto.getMeetingId())
                                                .userYCoordinate(submitVoteServiceDto.getYCoordinate())
                                                .userXCoordinate(submitVoteServiceDto.getXCoordinate())
                                                .earnedPoint(100)
                                                .build();

                                        return messageUtil.sendPointEarnedMessage(pointEarnedMessage);
                                    }

                                    return Mono.just(false);
                                });
                    }
                    else{
                        throw new DistanceNotCloseException("distance between the meeting point is " + distance);
                    }

                })
                .onErrorResume(throwable -> {
                    if(throwable instanceof DataIntegrityViolationException)
                    {
                        return Mono.error(new UserAlreadyJoinedException("the user " + submitVoteServiceDto.getUserId() +  " already joined the meeting " + submitVoteServiceDto.getMeetingId()));

                    }

                    return Mono.error(throwable);
                });
    }

    public Mono<Integer> createAttendance(Long meetingId, Long userId)
    {
        PlanJoinMeeting planJoinMeeting = PlanJoinMeeting.builder()
                .meetingId(meetingId)
                .userId(userId)
                .build();

        return planJoinMeetingRepository.save(planJoinMeeting)
                .onErrorResume(throwable -> {

                    if(throwable instanceof DataIntegrityViolationException)
                    {
                        log.error(throwable.getMessage());
                        Mono.error(new ResourceNotFoundException("meeting " + meetingId + " does not exist!"));
                    }

                    return Mono.error(throwable);
                });
    }

    public Mono<Integer> deleteAttendance(Long meetingId, Long userId)
    {
        return planJoinMeetingRepository.delete(meetingId, userId);
    }

    private static final double r2d = 180.0D / 3.141592653589793D;
    private static final double d2r = 3.141592653589793D / 180.0D;
    private static final double d2km = 111189.57696D * r2d;

    private static double meters(double lt1, double ln1, double lt2, double ln2) {
        double x = lt1 * d2r;
        double y = lt2 * d2r;
        return Math.acos( Math.sin(x) * Math.sin(y) + Math.cos(x) * Math.cos(y) * Math.cos(d2r * (ln1 - ln2))) * d2km;
    }




}
