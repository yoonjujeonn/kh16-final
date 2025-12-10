package com.kh.fd.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kh.fd.dao.BannerDao;
import com.kh.fd.dto.BannerDto;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.service.AttachmentService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "배너 관리 컨트롤러")
@RestController
@RequestMapping("/banner")
@CrossOrigin(origins = "http://localhost:5173")
public class BannerRestController {

    @Autowired
    private BannerDao bannerDao;

    @Autowired
    private AttachmentService attachmentService;

    // 배너 등록
    @PostMapping("/add")
    public void add(@ModelAttribute BannerDto bannerDto,@RequestParam(required = false) MultipartFile attach) throws IOException {

        int bannerNo = bannerDao.sequence();
        bannerDto.setBannerNo(bannerNo);

        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            bannerDto.setBannerAttachmentNo(attachmentNo);
        }

        bannerDao.insert(bannerDto);
    }

    // 목록
    @GetMapping("/list")
    public List<BannerDto> list() {
        return bannerDao.selectList();
    }

    // 상세
    @GetMapping("/{bannerNo}")
    public BannerDto detail(@PathVariable int bannerNo) {
        BannerDto bannerDto = bannerDao.selectOne(bannerNo);
        if (bannerDto == null) throw new TargetNotFoundException();
        return bannerDto;
    }

    @DeleteMapping("/{bannerNo}")
    public void delete(@PathVariable int bannerNo) {
        BannerDto bannerDto = bannerDao.selectOne(bannerNo);
        if (bannerDto == null) throw new TargetNotFoundException();

        //배너 삭제
        bannerDao.delete(bannerNo);

        //첨부파일 삭제
        if (bannerDto.getBannerAttachmentNo() != null) {
            attachmentService.delete(bannerDto.getBannerAttachmentNo());
        }
    }
}    
