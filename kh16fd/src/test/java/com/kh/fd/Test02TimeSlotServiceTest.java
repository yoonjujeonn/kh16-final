package com.kh.fd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.fd.service.SlotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class Test02TimeSlotServiceTest {
	@Autowired
	private SlotService slotService;
	
	@Test
	public void Test() {
		long no = 42;
		slotService.createTimeSlots(no);
		log.debug("성공");
	}
}
