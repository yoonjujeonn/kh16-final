package com.kh.fd.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DeleteMemberService {
	@Scheduled(cron = "0 30 12-18 * * *")
	public void work() {
		System.out.println(LocalDateTime.now() + "에 실행됨!");
		
	}
}
