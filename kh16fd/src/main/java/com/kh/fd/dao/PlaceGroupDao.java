package com.kh.fd.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.PlaceGroupDto;

@Repository
public class PlaceGroupDao {
	@Autowired
	private SqlSession sqlSession;
	
	public long sequence() {
		return sqlSession.selectOne("placeGroup.sequence");
	}
	
	public void insert(PlaceGroupDto placeGroupDto) {
		sqlSession.insert("placeGroup.insert", placeGroupDto);
	}
	
	public PlaceGroupDto selectByName(String depth1) {
		Map<String, Object> params = new HashMap<>();
		params.put("depth1", depth1);
		
		return sqlSession.selectOne("placeGroup.selectByName", params);
	}
	
}
