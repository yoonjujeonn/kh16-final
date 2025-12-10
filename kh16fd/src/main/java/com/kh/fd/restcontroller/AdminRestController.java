package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dto.RestaurantDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequestMapping("/admin")
public class AdminRestController {
	@Autowired
	private RestaurantDao restaurantDao;
	
	//승인 안된 식당 리스트
	@GetMapping("/")
	public List<RestaurantDto> approvalList(){
		return restaurantDao.selectApprovalList();
	}
	//상세 조회
	@PostMapping("/{restaurantId}")
	public RestaurantDto detail(@PathVariable long restaurantId) {
		return restaurantDao.selectOne(restaurantId);
	}
	
	
	
}
