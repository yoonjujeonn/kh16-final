package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.fd.dao.PaymentDao;
import com.kh.fd.dao.PaymentDetailDao;
import com.kh.fd.dao.ReservationDao;
import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dto.PaymentDetailDto;
import com.kh.fd.dto.PaymentDto;
import com.kh.fd.dto.ReservationDto;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.error.ReservationConflictException;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.vo.ReservationReadyVO;
import com.kh.fd.vo.kakaopay.KakaoPayApproveResponseVO;
import com.kh.fd.vo.kakaopay.KakaoPayFlashVO;

import lombok.extern.slf4j.Slf4j;

//결제 및 예약 동시 처리를 위해 통합
@Slf4j
@Service
public class ReservationService {
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private RestaurantDao restaurantDao;
	
	
	//결제 시작 전 중복 체크 & 식당 정보 제공
	public ReservationReadyVO checkConflictAndSendTarget(ReservationDto reservationDto) {
		
		boolean isAvailable = reservationDao.checkAvailableSlot(reservationDto);
		
		//중복 검사 결과가 false인 경우 예외 처리(예약 충돌)
		if(!isAvailable) throw new ReservationConflictException("이미 예약된 좌석입니다");
		
		Long restaurantId = reservationDto.getReservationTarget();
		
		RestaurantDto target = restaurantDao.selectOne(restaurantId);
		
		ReservationReadyVO readyVO = ReservationReadyVO.builder()
				.restaurantDto(target)
				.reservationId(reservationDao.sequence())
				.build();
		return readyVO;
	}
	
	//DB에 예약 정보 등록
	@Transactional
	public void createReservation(ReservationDto reservationDto) {
		
		//DB 저장 전에도 중복 예약 여부 확인
		boolean isAvailable = reservationDao.checkAvailableSlot(reservationDto);
		
		//중복 검사 결과가 false인 경우 예외 처리(예약 충돌)
		if(!isAvailable) throw new ReservationConflictException("이미 예약된 좌석입니다");
		
		
		reservationDao.insert(reservationDto);
		
	}
	
}
