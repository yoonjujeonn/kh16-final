package com.kh.fd.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @Builder @NoArgsConstructor
public class ReviewListVO {
	public Long restaurantId;
	public double restaurantAvgRating;
	public int reviewCount;
}
