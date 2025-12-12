package com.kh.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.fd.dao.CategoryMappingDao;
import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.vo.RestaurantRegisterVO;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private CategoryMappingDao categoryMappingDao; 

        @Transactional
        public long createRestaurant(RestaurantRegisterVO restaurantRegisterVO) {

            String address = restaurantRegisterVO.getRestaurantAddress();
            long placeId = placeService.createPlaceAndMapping(address);
            restaurantRegisterVO.setRestaurantPlace(placeId);

            RestaurantDto dto = RestaurantDto.builder()
                    .ownerId(restaurantRegisterVO.getOwnerId())
                    .restaurantPlace(restaurantRegisterVO.getRestaurantPlace())
                    .restaurantName(restaurantRegisterVO.getRestaurantName())
                    .restaurantOpen(restaurantRegisterVO.getRestaurantOpen())
                    .restaurantClose(restaurantRegisterVO.getRestaurantClose())
                    .restaurantBreakStart(restaurantRegisterVO.getRestaurantBreakStart())
                    .restaurantBreakEnd(restaurantRegisterVO.getRestaurantBreakEnd())
                    .restaurantLastOrder(restaurantRegisterVO.getRestaurantLastOrder())
                    .reservationInterval(restaurantRegisterVO.getReservationInterval())
                    .restaurantContact(restaurantRegisterVO.getRestaurantContact())
                    .restaurantAddress(restaurantRegisterVO.getRestaurantAddress())
                    .restaurantAddressX(restaurantRegisterVO.getRestaurantAddressX())
                    .restaurantAddressY(restaurantRegisterVO.getRestaurantAddressY())
                    .restaurantDescription(restaurantRegisterVO.getRestaurantDescription())
                    .restaurantOpeningDays(restaurantRegisterVO.getRestaurantOpeningDays())
                    .build();

            RestaurantDto result = restaurantDao.insert(dto);
            long restaurantId = result.getRestaurantId();

            // 카테고리 매핑 등록
            if (restaurantRegisterVO.getCategoryIdList() != null) {
                for (Long categoryNo : restaurantRegisterVO.getCategoryIdList()) {
                    categoryMappingDao.insert(restaurantId, categoryNo);
                }
            }
            return restaurantId;
    }
}