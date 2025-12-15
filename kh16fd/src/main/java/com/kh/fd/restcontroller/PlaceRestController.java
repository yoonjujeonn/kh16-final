package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kh.fd.dao.PlaceDao;

@RestController
@RequestMapping("/place")
@CrossOrigin
public class PlaceRestController {

    @Autowired
    private PlaceDao placeDao;

    // depth1 목록 (서울, 경기 등)
    @GetMapping("/depth1")
    public List<String> getDepth1List() {
    	 return placeDao.selectUpperList();
    }
    
    @GetMapping("/upper")
    public List<String> upperList() {
        return placeDao.selectUpperList();
    }

    @GetMapping("/lower/{upper}")
    public List<String> lowerList(@PathVariable String upper) {
        return placeDao.selectLowerList(upper);
    }
}
