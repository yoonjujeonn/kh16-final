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
		String origin = memberDto.getMemberPw();
		String encoded = passwordEncoder.encode(origin); //암호화 작업 수행
		memberDto.setMemberPw(encoded); //암호화한 비번으로 바꿔치기
		
		sqlSession.insert("member.insert", memberDto);
	}
	
	public void insertOwner(MemberDto memberDto) {
		String origin = memberDto.getMemberPw();
		String encoded = passwordEncoder.encode(origin); //암호화 작업 수행
		memberDto.setMemberPw(encoded); //암호화한 비번으로 바꿔치기
		memberDto.setMemberLevel("자영업자"); //자영업자로 만들기
		
		sqlSession.insert("member.insertWithLevel", memberDto);		
	}
	
	public void insertAdmin(MemberDto memberDto) {
		String origin = memberDto.getMemberPw();
		String encoded = passwordEncoder.encode(origin); //암호화 작업 수행
		memberDto.setMemberPw(encoded); //암호화한 비번으로 바꿔치기
		memberDto.setMemberLevel("관리자"); //자영업자로 만들기
		
		sqlSession.insert("member.insertWithLevel", memberDto);		
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
	
	public boolean updateLoginTime(String memberId) {
		return sqlSession.update("member.updateLoginTime", memberId) > 0;
	}
	public boolean updateChangeTime(String memberId) {
		return sqlSession.update("member.updateChangeTime", memberId) > 0;
	}
	public boolean updateWithdrawTime(String memberId) {
		return sqlSession.update("member.updateWithdrawTime", memberId) > 0;
	}
	
}
