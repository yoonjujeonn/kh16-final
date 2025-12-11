package com.kh.fd.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.vo.PageVO;
import com.kh.fd.vo.RestaurantListVO;

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
	
	public List<RestaurantDto> selectApprovalList(PageVO pageVO){
		Map<String, Integer> params = new HashMap<>();
		params.put("begin", pageVO.getBegin());
		params.put("end", pageVO.getEnd());
		return sqlSession.selectList("restaurant.listNeedApprove", params);
	}
	
	//카테고리 + 지역그룹 포함 식당 목록
	public List<RestaurantListVO> selectListWithCategory(PageVO pageVO){
		Map<String, Integer> params = new HashMap<>();
		params.put("begin", pageVO.getBegin());
		params.put("end", pageVO.getEnd());
		
		return sqlSession.selectList("restaurant.listWithCategory", params);
	}
	
	//페이지 계산(목록)
	public int listCount() {
		return sqlSession.selectOne("restaurant.listCount");
	}
	
	//페이지 계산(승인해야할 식당 목록)
	public int approvalCount() {
		return sqlSession.selectOne("restaurant.approvalListCount");
	}
}
