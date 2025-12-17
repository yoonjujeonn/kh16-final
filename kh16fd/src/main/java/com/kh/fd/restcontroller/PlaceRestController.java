package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kh.fd.dao.PlaceDao;
import com.kh.fd.dto.PlaceDto;
import com.kh.fd.vo.PlaceImageVO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "지역 컨트롤러")
@RestController
@RequestMapping("/place")
@CrossOrigin
public class PlaceRestController {

    @Autowired
    private PlaceDao placeDao;

    // depth1 목록 (서울, 경기 등)<문자열>
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
    
    //deth1 별 지역 목록 조회
    @GetMapping("/depth1/list")
    public List<PlaceDto> depth1RepresentativeList() {
        return placeDao.selectDepth1RepresentativeList();
    }
    
    @GetMapping("/top")
    public List<PlaceImageVO> topList() {
        return placeDao.selectDepth1WithImage();
    }
}
