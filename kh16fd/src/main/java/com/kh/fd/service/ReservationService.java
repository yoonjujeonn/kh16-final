package com.kh.fd.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.fd.dao.PaymentDao;
import com.kh.fd.dao.ReservationDao;
import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dao.SlotLockDao;
import com.kh.fd.dto.PaymentDto;
import com.kh.fd.dto.ReservationDto;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.error.ReservationConflictException;
import com.kh.fd.vo.ReservationReadyVO;

import lombok.extern.slf4j.Slf4j;

//결제 및 예약 동시 처리를 위해 통합
@Slf4j
@Service
public class ReservationService {
	@Autowired
	private ReservationDao reservationDao;

	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private SlotLockDao slotLockDao;
	
	// 결제 시작 전 중복 체크 & 식당 정보 제공
	public ReservationReadyVO checkConflictAndSendTarget(ReservationDto reservationDto) {

		boolean isAvailable = reservationDao.checkAvailableSlot(reservationDto);

		// 중복 검사 결과가 false인 경우 예외 처리(예약 충돌)
		if (!isAvailable)
			throw new ReservationConflictException("이미 예약된 좌석입니다");

		Long restaurantId = reservationDto.getReservationTarget();

		RestaurantDto target = restaurantDao.selectOne(restaurantId);

		ReservationReadyVO readyVO = ReservationReadyVO.builder().restaurantDto(target)
				.reservationId(reservationDao.sequence()).build();
		return readyVO;
	}

	// DB에 예약 정보 등록
	@Transactional
	public void createReservation(ReservationDto reservationDto) {

		// DB 저장 전에도 중복 예약 여부 확인
		boolean isAvailable = reservationDao.checkAvailableSlot(reservationDto);

		// 중복 검사 결과가 false인 경우 예외 처리(예약 충돌)
		if (!isAvailable)
			throw new ReservationConflictException("이미 예약된 좌석입니다");

		reservationDao.insert(reservationDto);
		
		long seatId = reservationDto.getReservationSeat();
		LocalDateTime reservationTime = reservationDto.getReservationTime();
		
		long slotLockId = slotLockDao.selectOneByReservation(seatId, reservationTime);
		
		//lock 제거
		slotLockDao.deleteLock(slotLockId);
	}

	@Transactional
	public void cancelReservation(Long reservationId) {
		// 예약 정보 조회
		ReservationDto reservationDto = reservationDao.selectOne(reservationId);

		// 환불 퍼센트 계산
		LocalDate today = LocalDate.now();
		LocalDate visitDate = reservationDto.getReservationTime().toLocalDate();
		int percent;

		if (today.isBefore(visitDate.minusDays(1))) {
			percent = 100;
		} else if (today.isEqual(visitDate.minusDays(1))) {
			percent = 50;
		} else {
			percent = 0;
		}
		
		//해당 예약의 결제 정보 가져오기
		PaymentDto paymentDto = paymentDao.selectOneByReservationId(reservationId);
		
		//환불 금액 계산
		int refundAmount = (int) (paymentDto.getPaymentTotal() * (percent/100.0));
		
		//PaymentService를 통해 실제 결제 취소 진행
		paymentService.cancel(paymentDto.getPaymentNo(), refundAmount);
		
		//예약 상태 변경
		reservationDao.updateStatus(reservationId, "예약취소");

	}
	
	
}
