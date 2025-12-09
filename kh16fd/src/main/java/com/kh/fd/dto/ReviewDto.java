package com.kh.fd.dto;

import java.time.LocalDateTime;

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
    private LocalDateTime reviewCreatedAt;
    private LocalDateTime reviewUpdatedAt;
}
