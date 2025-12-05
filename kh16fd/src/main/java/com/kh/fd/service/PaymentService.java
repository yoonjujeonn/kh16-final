package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.fd.dao.PaymentDao;
import com.kh.fd.dao.PaymentDetailDao;
import com.kh.fd.dto.PaymentDetailDto;
import com.kh.fd.dto.PaymentDto;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.vo.kakaopay.KakaoPayApproveResponseVO;
import com.kh.fd.vo.kakaopay.KakaoPayFlashVO;
import com.kh.fd.vo.kakaopay.KakaoPayQtyVO;
//수정해야됨
@Service
public class PaymentService {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private PaymentDetailDao paymentDetailDao;
//	@Autowired
//	private RestaurantDao restaurantDao;
	
	@Transactional
	public void insert(KakaoPayApproveResponseVO responseVO, KakaoPayFlashVO flashVO) {
		// DB 저장 코드 (payment, payment_detail)
				long paymentNo = paymentDao.sequence();
				paymentDao.insert(PaymentDto.builder()
						.paymentNo(paymentNo)
						.paymentOwner(responseVO.getPartnerUserId())// 구매자
						.paymentTid(responseVO.getTid())// 거래번호
						.paymentName(responseVO.getItemName())// 결제이름
						.paymentTotal(responseVO.getAmount().getTotal())// 결제금액
						.paymentRemain(responseVO.getAmount().getTotal())// 취소가능금액
						.build());
				// 결제상세정보는 qtyList를 조회해서 생성하도록 처리
				for (KakaoPayQtyVO qtyVO : flashVO.getQtyList()) {
					long paymentDetailNo = paymentDetailDao.sequence();
//					RestaurantDto restaurantDto = res
//					GiftcardDto giftcardDto = giftcardDao.selectOne(qtyVO.getNo());// 상품조회
					paymentDetailDao.insert(PaymentDetailDto.builder()
							.paymentDetailNo(paymentDetailNo)// 결제상세번호
							.paymentDetailOrigin(paymentNo)// 결제대표번호
							.paymentDetailItemNo(qtyVO.getNo())// 상품번호
//							.paymentDetailItemName(giftcardDto.getGiftcardName())// 상품명
//							.paymentDetailItemPrice(giftcardDto.getGiftcardPrice())// 판매가
							.paymentDetailQty(qtyVO.getQty())// 구매수량
							.build());
				}
	}
	
	@Transactional
	public void cancel(long paymentNo) {
		// update payment set payment_remain=0 where payment_no=?
		paymentDao.cancel(paymentNo);
		// update payment_detail set payment_detail_status = '취소' where
		// payment_detail_origin=?
		paymentDetailDao.cancel(paymentNo);
	}
	
	

}
