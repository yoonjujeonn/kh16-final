package com.kh.fd.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TimeSlotDto {
	private Long timeSlotId;
	private Long timeSlotTarget;
	private LocalDateTime timeSlotTargetDate;
	private LocalDateTime timeSlotStart;
	private LocalDateTime timeSlotEnd;
}
