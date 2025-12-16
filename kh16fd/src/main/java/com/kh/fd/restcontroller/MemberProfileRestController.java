package com.kh.fd.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.fd.dao.MemberProfileDao;
import com.kh.fd.service.AttachmentService;
import com.kh.fd.service.MemberProfileService;
import com.kh.fd.service.TokenService;
import com.kh.fd.vo.MemberProfileVO;

import io.swagger.v3.oas.annotations.tags.Tag;

//(origins = "http://localhost:5173") 잘 안되면 이거 밑에 삽입할것
@Tag(name="멤버 프로필 컨트롤러")
@RestController
@RequestMapping("/memberProfile")
@CrossOrigin
public class MemberProfileRestController {
	@Autowired
	private MemberProfileDao memberProfileDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private MemberProfileService memberProfileService;
	
    private void checkLogin(String bearerToken) {
        tokenService.parse(bearerToken);
    }
    
    @PostMapping("/add")
    public void add(
    		@ModelAttribute MemberProfileVO memberProfileVO,
    		@RequestParam(required = false) MultipartFile attach,
    		@RequestHeader(value = "Authorization", required = false) String bearerToken
    ) throws IllegalStateException, IOException {
    	//로그인 검사
    	checkLogin(bearerToken);
    	
    	if(attach != null && !attach.isEmpty()) {
    		int attachmentNo = attachmentService.save(attach);
    		memberProfileVO.setAttachmentNo(attachmentNo);
    	}
    	
    	memberProfileService.saveProfile(memberProfileVO);
    	
    }
    
    @GetMapping("/{memberId}")
    public MemberProfileVO detail(@PathVariable String memberId) {
    	MemberProfileVO profile = memberProfileDao.selectOne(memberId);
    	return profile;
    }
    
}
