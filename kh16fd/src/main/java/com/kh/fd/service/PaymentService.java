package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.fd.dao.PaymentDao;
import com.kh.fd.dao.PaymentDetailDao;
import com.kh.fd.dto.PaymentDetailDto;
import com.kh.fd.dto.PaymentDto;
import com.kh.fd.dto.ReservationDto;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.vo.kakaopay.KakaoPayApproveResponseVO;
import com.kh.fd.vo.kakaopay.KakaoPayCancelRequestVO;
import com.kh.fd.vo.kakaopay.KakaoPayCancelResponseVO;
import com.kh.fd.vo.kakaopay.KakaoPayFlashVO;
import com.kh.fd.vo.kakaopay.KakaoPayQtyVO;
//수정해야됨
@Service
public class PaymentService {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private PaymentDetailDao paymentDetailDao;
	@Autowired
	private KakaoPayService kakaoPayService;
//	@Autowired
//	private RestaurantDao restaurantDao;
//	@Autowired
//	private ReservationDao reservationDao;
	
	@Transactional
	public void insert(KakaoPayApproveResponseVO responseVO, KakaoPayFlashVO flashVO) {
		
		Long reservationId = flashVO.getReservationId();
//		ReservationDto reservationDto = reservationDao.selectOne(reservationId);
//		if(reservationDto==null) {
//			throw new TargetNotFoundException("존재하지 않는 예약정보입니다");
//		}
//		RestaurantDto restaurantDto = restaurantDao.selectOne(reservationDto.getReservationRestaurant());
//		if(restaurantDto == null) {
//			throw new TargetNotFoundException("결제된 레스토랑 정보가 존재하지 않습니다");
//		}
//		String paymentName = restaurantDto.getRestaurantName() + "예약 결제";
		
				long paymentNo = paymentDao.sequence();
				paymentDao.insert(PaymentDto.builder()
						.paymentNo(paymentNo)
						.paymentOwner(responseVO.getPartnerUserId())// 구매자
						.paymentTid(responseVO.getTid())// 거래번호
//						.paymentName(paymentName)// 결제이름
						.paymentTotal(responseVO.getAmount().getTotal())// 결제금액
						.paymentRemain(responseVO.getAmount().getTotal())// 취소가능금액
						.reservationId(reservationId)
						.build());
				// 결제상세정보는 qtyList를 조회해서 생성하도록 처리
					long paymentDetailNo = paymentDetailDao.sequence();
					int itemPrice = responseVO.getAmount().getTotal();
					
					paymentDetailDao.insert(PaymentDetailDto.builder()
							.paymentDetailNo(paymentDetailNo)// 결제상세번호
							.paymentDetailOrigin(paymentNo)// 결제대표번호
							.paymentDetailItemNo(reservationId)// 상품번호
//							.paymentDetailItemName(paymentName)// 상품명
							.paymentDetailItemPrice(itemPrice)// 판매가
							.paymentDetailQty(1)// 구매수량
							.build());
	}
	
	@Transactional
	public void cancel(long paymentNo) {
		paymentDao.cancel(paymentNo);
		paymentDetailDao.cancel(paymentNo);
	}
}
