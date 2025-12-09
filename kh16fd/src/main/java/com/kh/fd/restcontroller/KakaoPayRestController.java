package com.kh.fd.restcontroller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.dto.ReservationDto;
import com.kh.fd.service.KakaoPayService;
import com.kh.fd.service.PaymentService;
import com.kh.fd.vo.TokenVO;
import com.kh.fd.vo.kakaopay.KakaoPayApproveRequestVO;
import com.kh.fd.vo.kakaopay.KakaoPayApproveResponseVO;
import com.kh.fd.vo.kakaopay.KakaoPayFlashVO;
import com.kh.fd.vo.kakaopay.KakaoPayReadyRequestVO;
import com.kh.fd.vo.kakaopay.KakaoPayReadyResponseVO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/kakaopay")
public class KakaoPayRestController {
	@Autowired
	private KakaoPayService kakaoPayService;
	@Autowired
	private PaymentService paymentService;
	
	private Map<String, KakaoPayFlashVO> flashMap = Collections.synchronizedMap(new HashMap<>());
	
	@PostMapping("/buy")
	public KakaoPayReadyResponseVO buy (
			@RequestBody ReservationDto reservationDto, 
			@RequestHeader("Frontend-Url") String frontendUrl, 
			@RequestAttribute TokenVO tokenVO
			) {
		Long reservationId = reservationDto.getReservationId();
		Long restaurantId = reservationDto.getReservationTarget();
		
//		예약금 조회 구문
//		int total = paymentService.getRestaurantReservationPrice(restaurantId);
		
		int total = 10000;//예시 예약금 일단 하드코딩
		String itemName = "예약 #" + reservationId + " 결제";
		
		KakaoPayReadyRequestVO requestVO = KakaoPayReadyRequestVO.builder()
				.partnerOrderId(UUID.randomUUID().toString())
				.partnerUserId(tokenVO.getLoginId())
				.itemName(itemName)
				.totalAmount(total)
				.build();
		KakaoPayReadyResponseVO responseVO = kakaoPayService.ready(requestVO);
		
		flashMap.put(requestVO.getPartnerOrderId(), 
				KakaoPayFlashVO.builder()
				.partnerOrderId(requestVO.getPartnerOrderId())
				.partnerUserId(requestVO.getPartnerUserId())
				.tid(responseVO.getTid())
				.returnUrl(frontendUrl)
				.reservationId(reservationId)
				.build());
		return responseVO;
	}
//	@PostMapping("/buy/{reservationId}")
//	public KakaoPayReadyResponseVO buy (
//			@PathVariable Long reservationId, 
//			@RequestHeader("Frontend-Url") String frontendUrl, 
//			@RequestAttribute TokenVO tokenVO
//			) {
//		int total = 10000;//예시 예약금 일단 하드코딩
//		String itemName = "예약 #" + reservationId + " 결제";
//		
//		KakaoPayReadyRequestVO requestVO = KakaoPayReadyRequestVO.builder()
//				.partnerOrderId(UUID.randomUUID().toString())
//				.partnerUserId(tokenVO.getLoginId())
//				.itemName(itemName)
//				.totalAmount(total)
//				.build();
//		KakaoPayReadyResponseVO responseVO = kakaoPayService.ready(requestVO);
//		
//		flashMap.put(requestVO.getPartnerOrderId(), 
//				KakaoPayFlashVO.builder()
//				.partnerOrderId(requestVO.getPartnerOrderId())
//				.partnerUserId(requestVO.getPartnerUserId())
//				.tid(responseVO.getTid())
//				.returnUrl(frontendUrl)
//				.reservationId(reservationId)
//				.build());
//		return responseVO;
//	}
	@GetMapping("/buy/success/{partnerOrderId}")
	public void success(@PathVariable String partnerOrderId,
			@RequestParam("pg_token") String pgToken, HttpServletResponse response) throws IOException {
		KakaoPayFlashVO flashVO = flashMap.remove(partnerOrderId);
		//결제 승인을 위한 데이터 생성
				KakaoPayApproveRequestVO requestVO = KakaoPayApproveRequestVO.builder()
							.partnerOrderId(flashVO.getPartnerOrderId())
							.partnerUserId(flashVO.getPartnerUserId())
							.tid(flashVO.getTid())
							.pgToken(pgToken)
						.build();
				
				//결제 승인 요청
				KakaoPayApproveResponseVO responseVO = kakaoPayService.approve(requestVO);
				
				//DB 저장 코드 (payment, payment_detail)
				paymentService.insert(responseVO, flashVO);
				
				response.sendRedirect(flashVO.getReturnUrl() + "/success");
	}
	
	@GetMapping("/buy/cancel/{partnerOrderId}")
	public void cancel(@PathVariable String partnerOrderId, HttpServletResponse response) throws IOException {
		KakaoPayFlashVO flashVO = flashMap.remove(partnerOrderId);
		response.sendRedirect(flashVO.getReturnUrl() + "/cancel");
	}
	
	@GetMapping("/buy/fail/{partnerOrderId}")
	public void fail(@PathVariable String partnerOrderId, HttpServletResponse response) throws IOException {
		KakaoPayFlashVO flashVO = flashMap.remove(partnerOrderId);
		response.sendRedirect(flashVO.getReturnUrl() + "/fail");
	}

}
