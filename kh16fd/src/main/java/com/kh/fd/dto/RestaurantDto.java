package com.kh.fd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private int restaurantId;
    private String ownerId;
    private int restaurantPlace;
    private String restaurantName;
    private java.sql.Timestamp restaurantOpen;
    private java.sql.Timestamp restaurantClose;
    private java.sql.Timestamp restaurantBreakStart;
    private java.sql.Timestamp restaurantBreakEnd;
    private int reservationInterval;
    private String restaurantContact;
    private String restaurantAddress;
    private double restaurantAddressX;
    private double restaurantAddressY;
    private String restaurantDescription;
    private String restaurantOpeningHours;
    private String restaurantStatus;
    private double restaurantAvgRating;
    private Integer restaurantReservationPrice;
    private java.sql.Timestamp restaurantCreatedAt;
}
