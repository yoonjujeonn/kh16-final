package com.kh.fd.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.RestaurantHolidayDto;

@Repository
public class RestaurantHolidayDao {
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(RestaurantHolidayDto restaurantHolidayDto) {
		sqlSession.insert("restaurantHoliday.insert", restaurantHolidayDto);
	}
	
	public List<RestaurantHolidayDto> selectList(long restaurantId){
		return sqlSession.selectList("restaurantHoliday.list", restaurantId);
	}
}
