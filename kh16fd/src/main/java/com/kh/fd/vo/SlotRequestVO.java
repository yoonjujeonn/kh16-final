package com.kh.fd.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlotRequestVO {
	private Long restaurantId;
	private String slotTime;
	private Integer peopleCount;
}
