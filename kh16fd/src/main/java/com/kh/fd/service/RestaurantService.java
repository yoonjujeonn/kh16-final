package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dto.RestaurantDto;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class RestaurantService {
	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private PlaceService placeService;
	
	//attachmentService 생성 후 대표 이미지 설정 기능 추가
	@Transactional
	public long createRestaurant(RestaurantDto restaurantDto) {
		
		String address = restaurantDto.getRestaurantAddress();
		
		long placeId = placeService.createPlaceAndMapping(address);
		
		restaurantDto.setRestaurantPlace(placeId);
		
		restaurantDao.insert(restaurantDto);
		
		return restaurantDto.getRestaurantId();
	}
}
