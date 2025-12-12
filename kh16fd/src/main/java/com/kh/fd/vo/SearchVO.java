package com.kh.fd.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SearchVO {

    private String column;        // 검색 컬럼 (ex: restaurant_address)
    private String keyword;       // 검색 키워드 (ex: 강남)
    private Long placeGroupId;    // 선택한 그룹 (ex: 핫플 ID)
}
