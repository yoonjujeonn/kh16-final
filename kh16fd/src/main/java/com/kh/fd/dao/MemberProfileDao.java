package com.kh.fd.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.MemberProfileDto;

@Repository
public class MemberProfileDao {
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(String memberId, int memberProfileNo, MemberProfileDto memberProfileDto) {
		sqlSession.insert("memberProfile.insertProfile", memberProfileDto);
	}
	
	public int selectOne(String memberId) {
		return	sqlSession.selectOne("memberProfile.selectAttachmentNoByMemberId", memberId);
	}
	
	public void delete(String memberId) {
		sqlSession.delete("memberProfile.deleteByMemberId", memberId);
	}
	
}
