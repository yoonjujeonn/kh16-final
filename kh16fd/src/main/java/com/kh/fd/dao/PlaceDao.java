package com.kh.fd.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.PlaceDto;

@Repository
public class PlaceDao {

	@Autowired
	private SqlSession sqlSession;
	
	public long sequence() {
		return sqlSession.selectOne("place.sequence");
	}
	
	public void insert(PlaceDto placeDto) {
		sqlSession.insert("place.insert", placeDto);
	}
	
	// depth별 조회
	public PlaceDto selectByDepth(String depth1, String depth2, String depth3) {
		Map<String, Object> params = new HashMap<>();
		params.put("depth1", depth1);
		params.put("depth2", depth2);
		params.put("depth3", depth3);
		
		return sqlSession.selectOne("place.selectByDepth", params);
	}
	
    // 관리자 지역 매핑용
    public PlaceDto selectPlaceByRestaurantId(Long restaurantId) {
        return sqlSession.selectOne(
            "place.selectPlaceByRestaurantId",
            restaurantId
        );
    }
    public List<String> selectUpperList() {
        return sqlSession.selectList("place.upperPlaceList");
    }

    public List<String> selectLowerList(String upperPlace) {
        return sqlSession.selectList("place.lowerPlaceList", upperPlace);
    }
}