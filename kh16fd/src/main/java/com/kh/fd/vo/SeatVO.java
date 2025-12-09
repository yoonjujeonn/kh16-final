package com.kh.fd.vo;

import com.kh.fd.dto.SeatDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SeatVO {
	private SeatDto seatDto;
	private Integer count;
}
