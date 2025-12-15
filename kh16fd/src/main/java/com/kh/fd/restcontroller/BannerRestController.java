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
import com.kh.fd.service.TokenService;

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

    @Autowired
    private TokenService tokenService;

    private void checkLogin(String bearerToken) {
        tokenService.parse(bearerToken);
    }

    @PostMapping("/add")
    public void add(
        @ModelAttribute BannerDto bannerDto,
        @RequestParam(required = false) MultipartFile attach,
        @RequestHeader(value = "Authorization", required = false) String bearerToken
    ) throws IOException {

        // 로그인 검사
        checkLogin(bearerToken);

        int bannerNo = bannerDao.sequence();
        bannerDto.setBannerNo(bannerNo);

        // 첨부파일 저장
        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            bannerDto.setBannerAttachmentNo(attachmentNo);
        }

        // 배너 등록
        bannerDao.insert(bannerDto);
    }


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

    @DeleteMapping("/{bannerNo}")
    public void delete(
        @PathVariable int bannerNo,
        @RequestHeader("Authorization") String bearerToken
    ) {
        checkLogin(bearerToken);

        BannerDto bannerDto = bannerDao.selectOne(bannerNo);
        if (bannerDto == null)
            throw new TargetNotFoundException("존재하지 않는 배너 번호");

        bannerDao.delete(bannerNo);

        if (bannerDto.getBannerAttachmentNo() != null) {
            attachmentService.delete(bannerDto.getBannerAttachmentNo());
        }
    }
}
