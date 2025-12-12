package com.kh.fd.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantRegisterVO {

    private String ownerId;
    private Long restaurantId;
    private Long restaurantPlace;          
    private String restaurantName;
    private String restaurantOpen;
    private String restaurantClose;
    private String restaurantBreakStart;
    private String restaurantBreakEnd;
    private String restaurantLastOrder;
    private Integer reservationInterval;
    private String restaurantContact;
    private String restaurantAddress;
    private double restaurantAddressX;
    private double restaurantAddressY;
    private String restaurantDescription;
    private String restaurantOpeningDays;
    private Integer restaurantReservationPrice;
    private List<Long> categoryIdList;
}
