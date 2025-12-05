package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.fd.dao.PaymentDao;
import com.kh.fd.dao.PaymentDetailDao;

@Service
public class PaymentService {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private PaymentDetailDao paymentDetailDao;
	
	

}
