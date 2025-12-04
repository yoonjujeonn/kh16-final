package com.kh.fd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDto {
    private int wishlistNo;
    private String memberId;
    private int restaurantId;
    private java.sql.Timestamp createdAt;
}
