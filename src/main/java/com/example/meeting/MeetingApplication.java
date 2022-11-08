package com.example.meeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class MeetingApplication
{
	public static void main(String[] args) {
		SpringApplication.run(MeetingApplication.class, args);
	}
}
