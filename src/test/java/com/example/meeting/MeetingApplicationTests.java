package com.example.meeting;

import com.example.meeting.repository.JoinedMeetingRepository;
import com.example.meeting.repository.MeetingRepository;
import com.example.meeting.repository.PlanJoinMeetingRepository;
import com.example.meeting.repository.entity.Meeting;
import com.example.meeting.repository.entity.joinedMeeting.JoinedMeeting;
import com.example.meeting.repository.entity.planJoinMeeting.PlanJoinMeeting;
import com.example.meeting.service.MeetingService;
import com.example.meeting.service.dto.GetMeetingDetailServiceDto;
import com.example.meeting.service.dto.SaveMeetingServiceDto;
import com.example.meeting.service.dto.SubmitVoteServiceDto;
import com.example.meeting.util.messageUtil.MessageUtil;
import com.example.meeting.util.messageUtil.dto.PointEarnedMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@Slf4j
@EnableConfigurationProperties
@ActiveProfiles(profiles = "dev")
class MeetingApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	MessageUtil messageUtil;

	@Test
	void testMessageUtil(){
		PointEarnedMessage pointEarnedMessage = PointEarnedMessage.builder()
				.earnedPoint(100)
				.meetingId(Long.valueOf(3))
				.userId(Long.valueOf(2))
				.userXCoordinate(3.1151233)
				.userYCoordinate(5.99012333)
				.build();

		Mono<Boolean> sendResult = messageUtil.sendPointEarnedMessage(pointEarnedMessage);

		StepVerifier.create(sendResult).expectNext(true).verifyComplete();
	}

	@Autowired
	JoinedMeetingRepository joinedMeetingRepository;

	@Autowired
	MeetingRepository meetingRepository;

	@Test
	void testRepositories()
	{
		Flux<JoinedMeeting> meetings = joinedMeetingRepository.findAll().log();

		StepVerifier.create(meetings).expectNextCount(1).verifyComplete();
	}

	@Autowired
	MeetingService meetingService;

	@Test
	void testGetMeetings()
	{
		Flux<Meeting> meetingFlux = meetingService.getMeetingsByGroupId(Long.valueOf(10), PageRequest.of(0, 10));

		StepVerifier.create(meetingFlux)
				.expectNextCount(1)
				.verifyComplete();

	}

	@Test
	void testMeetingRepository()
	{
		Flux<Meeting> meetingFlux = meetingRepository.findByGroupId(Long.valueOf(3), PageRequest.of(0, 10)).log();

		StepVerifier.create(meetingFlux)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testSaveMeeting()
	{
		SaveMeetingServiceDto saveMeetingServiceDto = SaveMeetingServiceDto.builder()
				.meetingName("helloworld")
				.meetingXCoordinate(88.771234)
				.meetingYCoordinate(86.11234)
				.meetingLocation("서울시 성동구")
				.meetingCategory("축구")
				.meetingDate(LocalDate.of(2022, 03, 12))
				.meetingTime(LocalTime.of(13, 35))
				.groupId(Long.valueOf(10))
				.build();

		Mono<Meeting> saveMeetingMono = meetingService.saveMeeting(saveMeetingServiceDto).log();

		StepVerifier.create(saveMeetingMono)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testGetMeetingDetail()
	{
		Mono<GetMeetingDetailServiceDto> getMeetingDetailServiceDtoMono = meetingService.getMeetingDetail(Long.valueOf(1)).log();

		StepVerifier.create(getMeetingDetailServiceDtoMono)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testSubmitVote()
	{
		SubmitVoteServiceDto submitVoteServiceDto = SubmitVoteServiceDto.builder()
				.meetingId(Long.valueOf(1))
				.xCoordinate(3.1152333)
				.yCoordinate(116.22312)
				.userId(Long.valueOf(4))
				.build();

		Mono<Boolean> re = meetingService.submitVote(submitVoteServiceDto).log();

		StepVerifier.create(re)
				.expectComplete().verify();
	}

	@Autowired
	PlanJoinMeetingRepository planJoinMeetingRepository;

	@Test
	void testPlanJoinMeetingRepository()
	{

		PlanJoinMeeting planJoinMeeting = PlanJoinMeeting.builder()
				.userId(Long.valueOf(4))
				.meetingId(Long.valueOf(4))
				.build();

		Mono<Integer> planJoinMeetingMono = planJoinMeetingRepository.delete(Long.valueOf(4), Long.valueOf(4));

		StepVerifier.create(planJoinMeetingMono)
				.expectNextCount(1)
				.verifyComplete();
	}


}
