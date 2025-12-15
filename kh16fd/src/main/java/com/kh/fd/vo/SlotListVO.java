package com.kh.fd.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlotListVO {
	private Long restaurantId;
	private String restaurantName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime reservationTime;
	private Integer totalSeatCount;
	private Integer reservedSeatCount;
}
