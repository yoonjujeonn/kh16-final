package com.kh.fd.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlotListVO {
	private Long restaurantId;
	private String restaurantName;
	private String restaurantOpen;
	private String restaurantClose;
	private String restaurantLastOrder;
	private String restaurantOpeningDays;
	private List<LocalDate> restaurantHolidayDate;
	private Integer restaurantReservationPrice;
	private Long reservationTarget;
	private Integer reservationPeopleCount;
	private String reservationStatus;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime reservationTime;
	private Long reservationSeat;
	private Long seatId;
	private Long seatRestaurantId;
	private String seatType;
	private Integer seatMaxPeople;
	private Long reservedSeat;
}
