package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.fd.dao.SeatDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SlotService {
	@Autowired
	private SeatDao seatDao;
	
	
// 타임 슬롯 + 좌석 합쳐서 생성	
//	@Transactional
//	public long createTotalSlot() {
//		
//	}
}
