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
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.vo.kakaopay.KakaoPayApproveResponseVO;
import com.kh.fd.vo.kakaopay.KakaoPayCancelRequestVO;
import com.kh.fd.vo.kakaopay.KakaoPayFlashVO;

//수정해야됨
@Service
public class PaymentService {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private PaymentDetailDao paymentDetailDao;
	@Autowired
	private ReservationDao reservationDao;
	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private KakaoPayService kakaoPayService;
	
	@Transactional
	public void insert(KakaoPayApproveResponseVO responseVO, KakaoPayFlashVO flashVO) {
		
		Long reservationId = flashVO.getReservationId();
		ReservationDto reservationDto = reservationDao.selectOne(reservationId);
		
		if(reservationDto==null) {
			throw new TargetNotFoundException("존재하지 않는 예약정보입니다");
		}
		RestaurantDto restaurantDto = restaurantDao.selectOne(reservationDto.getReservationTarget());
		if(restaurantDto == null) {
			throw new TargetNotFoundException("결제된 레스토랑 정보가 존재하지 않습니다");
		}
		String paymentName = "No." + reservationId + restaurantDto.getRestaurantName() + "예약금 결제";
		
				long paymentNo = paymentDao.sequence();
				paymentDao.insert(PaymentDto.builder()
						.paymentNo(paymentNo)
						.paymentOwner(responseVO.getPartnerUserId())// 구매자
						.paymentTid(responseVO.getTid())// 거래번호
						.paymentName(paymentName)// 결제이름
						.paymentTotal(responseVO.getAmount().getTotal())// 결제금액
						.paymentRemain(responseVO.getAmount().getTotal())// 취소가능금액
						.reservationId(reservationId)
						.build());
				
					long paymentDetailNo = paymentDetailDao.sequence();
					int itemPrice = responseVO.getAmount().getTotal();
					
					
					paymentDetailDao.insert(PaymentDetailDto.builder()
							.paymentDetailNo(paymentDetailNo)// 결제상세번호
							.paymentDetailOrigin(paymentNo)// 결제대표번호
							.paymentDetailItemNo(reservationId)// 상품번호
							.paymentDetailItemName(paymentName)// 상품명
							.paymentDetailItemPrice(itemPrice)// 판매가
							.paymentDetailQty(1)// 구매수량
							.build());
					
	}
	
	//전액 취소
	@Transactional
	public void cancel(long paymentNo) {
		paymentDao.cancel(paymentNo);
		paymentDetailDao.cancel(paymentNo);
	}
	
	//금액 지정 취소
	@Transactional
	public void cancel(long paymentNo, int refundAmount) {
		//결제 정보 조회
		PaymentDto paymentDto =paymentDao.selectOne(paymentNo);
		
		//카카오페이서비스를 통해 실제 환불
		if(refundAmount>0) {
			kakaoPayService.cancel(KakaoPayCancelRequestVO.builder()
					.tid(paymentDto.getPaymentTid())
					.cancelAmount(refundAmount)
					.build());
		}
		
		paymentDao.cancel(paymentNo, refundAmount);
		paymentDetailDao.cancel(paymentNo);
	}
}
