package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.fd.dao.MemberProfileDao;
import com.kh.fd.vo.MemberProfileVO;

@Service
public class MemberProfileService {
	@Autowired
	private MemberProfileDao memberProfileDao; 
		
	//서비스서 할 일은 단 하나! 수정 경로를 만드는것
	
	@Transactional
	public void saveProfile(MemberProfileVO vo) {
	    // 1. 새로운 첨부파일 정보 등록
	    memberProfileDao.insertAttachment(vo); 
	    
	    // 2. 회원과 첨부파일 연결 (MERGE 실행)
	    memberProfileDao.updateAndInsert(vo);
	}
	
	
	
		
	
}
