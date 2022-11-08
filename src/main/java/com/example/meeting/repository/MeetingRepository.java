package com.example.meeting.repository;

import com.example.meeting.repository.entity.Meeting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MeetingRepository extends ReactiveCrudRepository<Meeting, Long>
{

    public Flux<Meeting> findByGroupId(Long groupId, Pageable pageable);


}
