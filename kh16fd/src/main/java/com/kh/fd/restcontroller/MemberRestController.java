package com.kh.fd.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.dao.MemberDao;
import com.kh.fd.dto.MemberDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "회원 관리 컨트롤러")

@CrossOrigin
@RestController
@RequestMapping("/account")
public class MemberRestController {
	@Autowired
	private MemberDao memberDao;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
	//차후 추가예정
//	@Autowired

	
	//회원가입
	//- @Valid가 붙음으로 인해 DTO에 설정된 Annotation이 일을 한다
	//-SpringDoc 설정을 추가하여 문서에 정보를 정확하게 기재한다
	@Operation(
			deprecated = false //비추천 여부 향후 사용 중지 예정이라면 true를 작성
			, description = "회원 가입을 위한 등록 기능" //기능에 대한 설명
			, responses = {//예상되는 응답 코드 
					@ApiResponse(responseCode = "200"), 
					@ApiResponse(responseCode = "400"),
					@ApiResponse(responseCode = "500")
			}
	)
	
	@PostMapping("/")
	public void add(@Valid @RequestBody MemberDto memberDto) {
		//등록 처리
		memberDao.insert(memberDto); 
	}
	
	//아이디 중복 검사
	// - 클라이언트에게 true(사용 가능) 또는 false(사용 중) 보내기
	//@GetMapping("/{accountId}") 이건 그냥 상세조회가 되어버림
	@GetMapping("/memberId/{memberId}") //커스텀 기능
	public boolean checkAccountId(@PathVariable String memberId) {
		MemberDto MemberDto  = memberDao.selectOne(memberId);
		return MemberDto == null;
	}
	
	
}
