package com.kh.fd.dto;

import java.time.LocalDate;
import java.time.LocalTime;

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
	private LocalDate timeSlotTargetDate;
	private LocalTime timeSlotStart;
	private LocalTime timeSlotEnd;
}
