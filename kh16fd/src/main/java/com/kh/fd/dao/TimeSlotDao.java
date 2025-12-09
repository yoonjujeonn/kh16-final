package com.kh.fd.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.TimeSlotDto;

@Repository
public class TimeSlotDao {
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(TimeSlotDto timeSlotDto) {
		timeSlotDto.setTimeSlotId(sqlSession.selectOne("timeSlot.sequence"));
		sqlSession.insert("timeSlot.insert", timeSlotDto);
	}
	
	public List<TimeSlotDto> selectList(long timeSlotTarget){
		return sqlSession.selectList("timeSlot.list", timeSlotTarget);
	}
}
