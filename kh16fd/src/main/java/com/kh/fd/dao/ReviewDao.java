package com.kh.fd.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.kh.fd.dto.ReviewDto;
import com.kh.fd.vo.TokenVO;

public class ReviewDao {

	@Autowired
	private SqlSession sqlSession;

	// 리뷰등록
	public void insert(ReviewDto reviewDto) {
		sqlSession.insert("review.insert", reviewDto);
	}

	// 특정 식당의 리뷰 목록 조회
	public List<ReviewDto> selectListByRestaurant(int restaurantNo) {
		return sqlSession.selectList("review.listByRestaurant", restaurantNo);
	}

	// 특정 사용자가 작성한 리뷰 목록 조회
	public List<ReviewDto> selectListByUser(TokenVO tokenVO) {
		return sqlSession.selectList("review.listByUser", tokenVO);
	}

	// 리뷰 상세 조회
	public ReviewDto selectOne(int reviewNo) {
		return sqlSession.selectOne("review.detail", reviewNo);
	}

	// 리뷰 수정
	public boolean update(ReviewDto reviewDto) {
		return sqlSession.update("review.update", reviewDto) > 0;
	}

	// 리뷰 삭제
	public boolean delete(int reviewNo) {
		return sqlSession.delete("review.delete", reviewNo) > 0;
	}

}
