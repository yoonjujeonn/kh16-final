package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.dao.PaymentDao;
import com.kh.fd.dao.PaymentDetailDao;
import com.kh.fd.dto.PaymentDetailDto;
import com.kh.fd.dto.PaymentDto;
import com.kh.fd.error.NeedPermissionException;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.service.KakaoPayService;
import com.kh.fd.service.PaymentService;
import com.kh.fd.vo.TokenVO;
import com.kh.fd.vo.kakaopay.KakaoPayCancelRequestVO;
import com.kh.fd.vo.kakaopay.KakaoPayCancelResponseVO;
import com.kh.fd.vo.kakaopay.KakaoPayOrderRequestVO;
import com.kh.fd.vo.kakaopay.KakaoPayOrderResponseVO;
import com.kh.fd.vo.kakaopay.PaymentInfoVO;

@CrossOrigin
@RestController
@RequestMapping("/payment")
public class PaymentRestController {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private PaymentDetailDao paymentDetailDao;
	@Autowired
	private KakaoPayService kakaoPayService;
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/account")
	public List<PaymentDto> listByOwner(@RequestAttribute TokenVO tokenVO){
		return paymentDao.selectList(tokenVO);
	}
	@GetMapping("/{paymentNo}")
	public PaymentInfoVO detail(@PathVariable long paymentNo,
			@RequestAttribute TokenVO tokenVO) {
		PaymentDto paymentDto = paymentDao.selectOne(paymentNo);
		if(paymentDto == null) throw new TargetNotFoundException();
		
		boolean isOwner = paymentDto.getPaymentOwner().equals(tokenVO.getLoginId());
		if(isOwner==false) throw new NeedPermissionException();
		
		List<PaymentDetailDto> paymentDetailList = paymentDetailDao.selectList(paymentNo);
		
		KakaoPayOrderResponseVO responseVO = kakaoPayService.order(KakaoPayOrderRequestVO.builder()
				.tid(paymentDto.getPaymentTid())
				.build());
		
		return PaymentInfoVO.builder()
				.paymentDto(paymentDto)
				.paymentDetailList(paymentDetailList)
				.responseVO(responseVO)
				.build();
	}
	
	@DeleteMapping("/{paymentNo}")
	public void cancel(@PathVariable long paymentNo,
			@RequestAttribute TokenVO tokenVO) {
		PaymentDto paymentDto = paymentDao.selectOne(paymentNo);
		if(paymentDto == null) throw new TargetNotFoundException();
		boolean isOwner = paymentDto.getPaymentOwner().equals(tokenVO.getLoginId());
		if(isOwner == false) throw new NeedPermissionException();
		
		KakaoPayCancelRequestVO requestVO = KakaoPayCancelRequestVO.builder()
				.tid(paymentDto.getPaymentTid())
				.cancelAmount(paymentDto.getPaymentRemain())
				.build();
		
		KakaoPayCancelResponseVO responseVO = kakaoPayService.cancel(requestVO);
		
		if(responseVO !=null) {
			paymentService.cancel(paymentNo);
		}else {
			throw new TargetNotFoundException();
		}
	}
}
