package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.fd.dao.MemberProfileDao;

@Service
public class MemberProfileService {
	@Autowired
	private MemberProfileDao memberProfileDao; 
		
	//서비스서 할 일은 단 하나! 수정 경로를 만드는것
	
//	@Transactional
//	public int saveProfile() {
		
//	}
	
	
	
		
	
}
