package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dao.RestaurantHolidayDao;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.dto.RestaurantHolidayDto;
import com.kh.fd.service.RestaurantService;
import com.kh.fd.service.TokenService;
import com.kh.fd.vo.TokenVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
@Tag(name = "식당 관리 컨트롤러")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/restaurant")
public class RestaurantRestController {
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RestaurantHolidayDao restaurantHolidayDao;
	
	@PostMapping("/")
	public RestaurantDto add(@RequestBody RestaurantDto restaurantDto, @RequestHeader("Authorization") String bearerToken) {
		TokenVO tokenVO = tokenService.parse(bearerToken);
		restaurantDto.setOwnerId(tokenVO.getLoginId());
		long restaurantId = restaurantService.createRestaurant(restaurantDto);
		return restaurantDao.selectOne(restaurantId);
	}
	
	@PostMapping("/holiday")
	public void add(@RequestBody List<RestaurantHolidayDto> holidays) {
		for(RestaurantHolidayDto holidayDto : holidays) {
			restaurantHolidayDao.insert(holidayDto);
		}
	}
}
