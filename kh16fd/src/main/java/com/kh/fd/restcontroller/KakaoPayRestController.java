package com.kh.fd.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.service.KakaoPayService;
import com.kh.fd.service.PaymentService;

@CrossOrigin
@RestController
@RequestMapping("/kakaopay")
public class KakaoPayRestController {
	@Autowired
	private KakaoPayService kakaoPayService;
	@Autowired
	private PaymentService paymentService;

}
