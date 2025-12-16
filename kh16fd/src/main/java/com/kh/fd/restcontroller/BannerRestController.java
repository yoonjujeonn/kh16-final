package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kh.fd.dao.BannerDao;
import com.kh.fd.dto.BannerDto;
import com.kh.fd.error.TargetNotFoundException;


import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "배너 관리 컨트롤러")
@RestController
@RequestMapping("/banner")
@CrossOrigin(origins = "http://localhost:5173")
public class BannerRestController {

    @Autowired
    private BannerDao bannerDao;

    @GetMapping("/list")
    public List<BannerDto> list() {
        return bannerDao.selectList();
    }

    @GetMapping("/{bannerNo}")
    public BannerDto detail(@PathVariable int bannerNo) {
        BannerDto bannerDto = bannerDao.selectOne(bannerNo);
        if (bannerDto == null) {
            throw new TargetNotFoundException("존재하지 않는 배너 번호");
        }
        return bannerDto;
    }
}