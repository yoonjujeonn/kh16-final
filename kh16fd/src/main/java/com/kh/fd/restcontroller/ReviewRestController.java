package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.dao.ReviewDao;
import com.kh.fd.dto.ReviewDto;

import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "리뷰 관리 컨트롤러")
@CrossOrigin
@RestController
@RequestMapping("/restaurant/detail/{restaurantId}/review")
public class ReviewRestController {
	@Autowired
	private ReviewDao reviewDao;
	
	//리뷰 등록
	@PostMapping("/")
	public void insert(
			@PathVariable int restaurantId,
			@RequestBody ReviewDto reviewDto) {
		reviewDto.setRestaurantId(restaurantId);
		reviewDao.insert(reviewDto);
	}
	
	//특정 식당의 리뷰 목록 조회
	@GetMapping("/")
	public List<ReviewDto> ListByRestaurant(@PathVariable int restaurantId){
		return reviewDao.selectListByRestaurant(restaurantId);
	}
	
	//리뷰 상세
	@GetMapping("/{reviewNo}")
	public ReviewDto detail(@PathVariable int restaurantId, @PathVariable int reviewNo) {
		return reviewDao.selectOne(reviewNo);
	}
	
	//리뷰 수정
	@PutMapping("/{reviewNo}")
	public boolean update(@PathVariable int restaurantId,
			@PathVariable int reviewNo,
			@RequestBody ReviewDto reviewDto) {
		reviewDto.setReviewNo(reviewNo);
		reviewDto.setRestaurantId(restaurantId);
		return reviewDao.update(reviewDto);
	}
	
	//리뷰 삭제
	@DeleteMapping("/{reviewNo}")
	public boolean delete(@PathVariable int restaurantId, 
			@PathVariable int reviewNo) {
		return reviewDao.delete(reviewNo);
	}
	
	

}
