package com.kh.fd.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kh.fd.dao.BannerDao;
import com.kh.fd.dao.CategoryDao;
import com.kh.fd.dao.PlaceDao;
import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dao.ReviewDao;
import com.kh.fd.dto.BannerDto;
import com.kh.fd.dto.CategoryDto;
import com.kh.fd.dto.PlaceImageDto;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.service.AttachmentService;
import com.kh.fd.vo.PageVO;
import com.kh.fd.vo.RestaurantApprovalListVO;
import com.kh.fd.vo.ReviewAdminVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 기능 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminRestController {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private BannerDao bannerDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ReviewDao reviewDao;
    
    @Autowired
    private PlaceDao placeDao;

    // 승인 안된 식당 리스트
    @GetMapping("/page/{page}")
    public RestaurantApprovalListVO approvalList(@PathVariable int page) {
        PageVO pageVO = new PageVO();
        pageVO.setPage(page);
        pageVO.setDataCount(restaurantDao.approvalCount());

        List<RestaurantDto> list = restaurantDao.selectApprovalList(pageVO);

        return RestaurantApprovalListVO.builder()
                .page(pageVO.getPage())
                .size(pageVO.getSize())
                .count(pageVO.getDataCount())
                .begin(pageVO.getBegin())
                .end(pageVO.getEnd())
                .last(pageVO.getPage() >= pageVO.getTotalPage())
                .list(list)
                .build();
    }

    // 상세 조회
    @PostMapping("/{restaurantId}")
    public RestaurantDto detail(@PathVariable long restaurantId) {
        return restaurantDao.selectOne(restaurantId);
    }

    // 관리자용 리뷰 전체 목록
    @GetMapping("/review/list")
    public List<ReviewAdminVO> reviewList() {
        return reviewDao.selectListAdmin();
    }

    // 관리자용 리뷰 삭제
    @DeleteMapping("/review/{reviewNo}")
    public boolean deleteReview(@PathVariable int reviewNo) {
        return reviewDao.delete(reviewNo);
    }

    // 배너 등록
    @PostMapping("/banner")
    public void addBanner(
            @ModelAttribute BannerDto bannerDto,
            @RequestParam(required = false) MultipartFile attach
    ) throws IOException {

        int bannerNo = bannerDao.sequence();
        bannerDto.setBannerNo(bannerNo);

        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            bannerDto.setBannerAttachmentNo(attachmentNo);
        }

        bannerDao.insert(bannerDto);
    }

    // 배너 삭제 
    @DeleteMapping("/banner/{bannerNo}")
    public void deleteBanner(@PathVariable int bannerNo) {

        BannerDto bannerDto = bannerDao.selectOne(bannerNo);
        if (bannerDto == null) {
            throw new TargetNotFoundException("존재하지 않는 배너 번호");
        }

        bannerDao.delete(bannerNo);

        if (bannerDto.getBannerAttachmentNo() != null) {
            attachmentService.delete(bannerDto.getBannerAttachmentNo());
        }
    }

    // 카테고리 등록
    @PostMapping("/category")
    public void addCategory(@RequestBody CategoryDto categoryDto) {
        int seq = (int) categoryDao.sequence();
        categoryDto.setCategoryNo(seq);
        categoryDao.insert(categoryDto);
    }

    // 카테고리 수정
    @PutMapping("/category/{categoryNo}")
    public void editCategory(
            @PathVariable int categoryNo,
            @RequestBody CategoryDto categoryDto
    ) {
        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }
        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }

    // 카테고리 삭제
    @DeleteMapping("/category/{categoryNo}")
    public void deleteCategory(@PathVariable int categoryNo) {
        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }
        categoryDao.delete(categoryNo);
    }

    // 카테고리 이미지 등록 
    @PostMapping("/category/image")
    public void addCategoryImage(
            @RequestParam int categoryNo,
            @RequestParam MultipartFile attach
    ) throws IOException {

        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }

        Integer oldAttachmentNo = categoryDao.selectCategoryImageAttachmentNo(categoryNo);

        if (oldAttachmentNo != null) {
            attachmentService.delete(oldAttachmentNo);
        }

        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            categoryDao.insertCategoryImage(categoryNo, attachmentNo);
        }
    }

    //카테고리 이미지 삭제
    @DeleteMapping("/category/image/{categoryNo}")
    public void deleteCategoryImage(@PathVariable int categoryNo) {

        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }

        Integer attachmentNo =
                categoryDao.selectCategoryImageAttachmentNo(categoryNo);

        if (attachmentNo != null) {
        	attachmentService.delete(attachmentNo);
        }
    }
    
    //지역 이미지 등록
    @PostMapping("/place/image")
    public void addPlaceImage(
            @RequestParam Long placeId,
            @RequestParam MultipartFile attach
    ) throws IOException {

        if (placeDao.selectOne(placeId) == null) {
            throw new TargetNotFoundException("존재하지 않는 지역");
        }

        Integer oldAttachmentNo = placeDao.selectPlaceImageAttachmentNo(placeId);

        if (oldAttachmentNo != null) {
            attachmentService.delete(oldAttachmentNo);
        }

        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach); 
            placeDao.insertPlaceImage(placeId, attachmentNo);
        }
    }
    //지역 이미지 삭제
    @DeleteMapping("/place/image/{placeId}")
    public void deletePlaceImage(@PathVariable Long placeId) {

        if (placeDao.selectOne(placeId) == null) {
            throw new TargetNotFoundException("존재하지 않는 지역");
        }

        Integer attachmentNo = placeDao.selectPlaceImageAttachmentNo(placeId);

        if (attachmentNo != null) {
            attachmentService.delete(attachmentNo);
        }
    }
}
