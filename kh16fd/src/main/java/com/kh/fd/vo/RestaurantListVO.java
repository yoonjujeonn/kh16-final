
package com.kh.fd.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RestaurantListVO {

    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;

    private Long placeId;
    private String placeDepth1;
    private String placeDepth2;
    private String placeDepth3;

    private Long placeGroupId;
    private Long parentGroupId;
    private String placeGroupName;

    private Long categoryId;
    private String categoryName;
}

