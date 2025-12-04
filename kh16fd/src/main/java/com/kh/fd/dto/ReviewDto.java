package com.kh.fd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private int reviewNo;
    private int restaurantId;
    private String memberId;
    private String reviewContent;
    private double reviewRating;
    private java.sql.Timestamp reviewCreatedAt;
}
