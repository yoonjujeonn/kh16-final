package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dao.ReviewDao;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.vo.PageVO;
import com.kh.fd.vo.RestaurantApprovalListVO;
import com.kh.fd.vo.ReviewAdminVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminRestController {
	@Autowired
	private RestaurantDao restaurantDao;

	@Autowired
	private ReviewDao reviewDao;

	// 승인 안된 식당 리스트
	@GetMapping("/page/{page}")
	public RestaurantApprovalListVO approvalList(@PathVariable int page) {
		PageVO pageVO = new PageVO();
		pageVO.setPage(page);
		pageVO.setDataCount(restaurantDao.approvalCount());

		List<RestaurantDto> list = restaurantDao.selectApprovalList(pageVO);

		return RestaurantApprovalListVO.builder().page(pageVO.getPage()).size(pageVO.getSize())
				.count(pageVO.getDataCount()).begin(pageVO.getBegin()).end(pageVO.getEnd())
				.last(pageVO.getPage() >= pageVO.getTotalPage()).list(list).build();
	}

	// 상세 조회
	@PostMapping("/{restaurantId}")
	public RestaurantDto detail(@PathVariable long restaurantId) {
		return restaurantDao.selectOne(restaurantId);
	}

	// 관리자용 리뷰 전체 목록 조회
	@GetMapping("/review/list")
	public List<ReviewAdminVO> reviewList() {
		return reviewDao.selectListAdmin();
	}

	// 관리자용 리뷰 삭제
	@DeleteMapping("/review/{reviewNo}")
	public boolean deleteReview(@PathVariable int reviewNo) {
		return reviewDao.delete(reviewNo);
	}

}
