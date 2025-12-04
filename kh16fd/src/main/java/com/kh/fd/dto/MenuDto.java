package com.kh.fd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private int menuId;
    private int restaurantId;
    private String menuName;
    private int menuPrice;
    private String menuInfo;
}
