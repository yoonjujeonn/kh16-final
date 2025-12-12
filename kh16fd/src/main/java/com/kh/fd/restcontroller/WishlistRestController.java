package com.kh.fd.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.fd.dao.WishlistDao;
import com.kh.fd.dto.WishlistDto;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "위시리스트 관리 컨트롤러")
@CrossOrigin
@RestController
@RequestMapping("/rest/wishlist")
public class WishlistRestController {

	@Autowired
	private WishlistDao wishlistDao;

	@PostMapping("/toggle")
	public Map<String, Object> toggle(@RequestBody WishlistDto wishlistDto) {
		boolean isCheck = wishlistDao.check(wishlistDto);

		if (isCheck) {
			wishlistDao.delete(wishlistDto);
		} else {
			wishlistDao.insert(wishlistDto);
		}

		int count = wishlistDao.countByRestaurantId(wishlistDto.getRestaurantId());

		Map<String, Object> result = new HashMap<>();
		result.put("isWish", !isCheck);
		result.put("count", count);

		return result;
	}

	@PostMapping("/check")
	public Map<String, Object> check(@RequestBody WishlistDto wishlistDto) {
		// 1. 내가 저장했는지 여부
		boolean isCheck = wishlistDao.check(wishlistDto);
		// 2. 이 식당의 전체 저장 개수
		int count = wishlistDao.countByRestaurantId(wishlistDto.getRestaurantId());

		Map<String, Object> result = new HashMap<>();
		result.put("isWish", isCheck);
		result.put("count", count);

		return result;
	}

}
