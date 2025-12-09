package com.kh.fd.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.fd.dao.BannerDao;
import com.kh.fd.dto.BannerDto;
import com.kh.fd.service.AttachmentService;

@RestController
@RequestMapping("/banner")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BannerRestController {

    @Autowired
    private BannerDao bannerDao;

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/add")
    public String add(
            @ModelAttribute BannerDto dto,
            @RequestParam(required = false) MultipartFile attach
    ) throws IOException {

        // 파일 업로드가 있는 경우 attachment 번호 생성
        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            dto.setBannerAttachmentNo(attachmentNo);
        }

        // JdbcTemplate은 시퀀스를 SQL 안에서 처리함
        bannerDao.insert(dto);

        return "OK";
    }
}
