package com.kh.fd.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.PlaceGroupMappingDto;

@Repository
public class PlaceGroupMappingDao {
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(PlaceGroupMappingDto placeGroupMappingDto) {
		sqlSession.insert("placeGroupMapping.insert", placeGroupMappingDto);
	}
	
	public boolean selectByMapping(long placeId, long placeGroupId) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("placeId", placeId);
		params.put("placeGroupId", placeGroupId);
		
		int count = sqlSession.selectOne("placeGroupMapping.selectByMapping", params);
		
		return count > 0;
	}
}
