package com.kh.fd.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TotalSlotDto {
	private Long totalSlotSeat;
	private Long totalSlotTime;
	private String totalSlotStatus;
	private String totalSlotLockedBy;
	private LocalDateTime totalSlotLockedAt;
}
