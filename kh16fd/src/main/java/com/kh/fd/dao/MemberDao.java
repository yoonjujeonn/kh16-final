package com.kh.fd.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.MemberDto;

@Repository
public class MemberDao {
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void insert(MemberDto memberDto) {
		//삽입 전 암호화 작업 필요
		//비밀번호 암호화 처리 추가 필요 현재 swagger 사용 못하는 오류로 봉인중
		String origin = memberDto.getMemberPw();
		String encoded = passwordEncoder.encode(origin); //암호화 작업 수행
		memberDto.setMemberPw(encoded); //암호화한 비번으로 바꿔치기
		
		sqlSession.insert("member.insert", memberDto);
	}
	
	public MemberDto selectOne(String memberId) {
		return sqlSession.selectOne("member.detail", memberId);
	}

	public MemberDto selectOneBymemberNickname(String memberNickname) {
		return sqlSession.selectOne("member.detailBymemberNickname", memberNickname);
	}
	
	public List<MemberDto> selectList(String column, String keyword) {
		Map<String, Object> params = new HashMap<>();
		params.put("column", column);
		params.put("keyword", keyword);
		return sqlSession.selectList("member.seasch", params);
	}	
	
	public boolean updateMember(MemberDto memberDto) {
		return sqlSession.update("member.updateMember", memberDto) > 0;
	}
	
}
