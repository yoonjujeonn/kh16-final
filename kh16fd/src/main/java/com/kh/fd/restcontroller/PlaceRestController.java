package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kh.fd.dao.PlaceDao;

@RestController
@RequestMapping("/place")
public class PlaceRestController {

    @Autowired
    private PlaceDao placeDao;

    // 상위 지역 조회
    @GetMapping("/upper")
    public List<String> upperPlaceList() {
        return placeDao.upperPlaceList();
    }

    // 하위 지역 목록 조회
    @GetMapping("/lower/{upperPlace}")
    public List<String> lowerPlaceList(@PathVariable String upperPlace) {
        return placeDao.lowerPlaceList(upperPlace);
    }
}
