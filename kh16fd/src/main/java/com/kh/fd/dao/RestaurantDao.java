package com.kh.fd.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.error.TargetNotFoundException;

@Repository
public class RestaurantDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public RestaurantDto insert(RestaurantDto restaurantDto) {
		long sequence = sqlSession.selectOne("restaurant.sequence");
		restaurantDto.setRestaurantId(sequence);
		sqlSession.insert("restaurant.insert", restaurantDto);
		return restaurantDto;
	}
	
	public RestaurantDto selectOne(long restaurantId) {
		RestaurantDto restaurantDto = sqlSession.selectOne("restaurant.detail", restaurantId);
		if(restaurantDto == null) throw new TargetNotFoundException();
		return restaurantDto;
	}
	
	public List<RestaurantDto> selectApprovalList(){
		return sqlSession.selectList("restaurant.listNeedApprove");
	}
}
