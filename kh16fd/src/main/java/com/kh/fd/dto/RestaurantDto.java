package com.kh.fd.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long restaurantId;
    private String ownerId;
    private Long restaurantPlace;
    private String restaurantName;
    private String restaurantOpen;
    private String restaurantClose;
    private String restaurantBreakStart;
    private String restaurantBreakEnd;
    private String restaurantLastOrder; 
    private int reservationInterval;
    private String restaurantContact;
    private String restaurantAddress;
    private double restaurantAddressX;
    private double restaurantAddressY;
    private String restaurantDescription;
    private String restaurantOpeningDays;
    private String restaurantStatus;
    private double restaurantAvgRating;
    private Integer restaurantReservationPrice;
    private LocalDateTime restaurantCreatedAt;
}
