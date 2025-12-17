package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.service.RestaurantService;
import com.kh.fd.vo.TokenVO;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/owner")
public class RestaurantOwnerRestController {
	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private RestaurantService restaurantService;
	
	//내가 등록한 식당 신청서 관리
	@GetMapping("/")
	public List<RestaurantDto> formList(TokenVO tokenVO){
		
		//나중에 OwnerInterceptor 처리
		List<RestaurantDto> list = restaurantDao.selectFormListByOwnerId(tokenVO.getLoginId());
		
		return list;
	}
	
	@GetMapping("/{restaurantId}")
	public RestaurantDto formDetail(TokenVO tokenVO, @PathVariable long restaurantId) {
		RestaurantDto restaurantDto = restaurantDao.selectFormByOwnerId(restaurantId, tokenVO.getLoginId());
		
		return restaurantDto;
	}
	
	@PutMapping("/edit/address/{restaurantId}")
	public RestaurantDto updateAddress(@RequestBody RestaurantDto restaurantDto) {
		RestaurantDto updatedDto = restaurantService.updateRestaurantPlace(restaurantDto);
		
		return updatedDto;
	}
	
	@PatchMapping("/edit/{restaurantId}")
	public void patch(@PathVariable long restaurantId, @RequestBody RestaurantDto restaurantDto) {
		restaurantDao.patchForm(restaurantDto);
	}
}
