package com.kh.fd.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kh.fd.dao.BannerDao;
import com.kh.fd.dao.CategoryDao;
import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dto.BannerDto;
import com.kh.fd.dto.CategoryDto;
import com.kh.fd.dao.ReviewDao;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.service.AttachmentService;
import com.kh.fd.vo.PageVO;
import com.kh.fd.vo.RestaurantApprovalListVO;
import com.kh.fd.vo.ReviewAdminVO;

import lombok.extern.slf4j.Slf4j;

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


	// 승인 안된 식당 리스트
		@GetMapping("/restaurant/page/{page}")
		public RestaurantApprovalListVO approvalList(@PathVariable int page) {
			PageVO pageVO = new PageVO();
			pageVO.setPage(page);
			pageVO.setDataCount(restaurantDao.approvalCount());

			List<RestaurantDto> list = restaurantDao.selectApprovalList(pageVO);

			return RestaurantApprovalListVO.builder().page(pageVO.getPage()).size(pageVO.getSize())
					.count(pageVO.getDataCount()).begin(pageVO.getBegin()).end(pageVO.getEnd())
					.last(pageVO.getPage() >= pageVO.getTotalPage()).list(list).build();
		}

		// 상세 조회
		@GetMapping("/restaurant/{restaurantId}")
		public RestaurantDto detail(@PathVariable long restaurantId) {
			return restaurantDao.selectFormByAdmin(restaurantId);
		}

		// 관리자용 리뷰 전체 목록 조회
		@GetMapping("/review/list")
		public List<ReviewAdminVO> reviewList() {
			return reviewDao.selectListAdmin();
		}

		// 관리자용 리뷰 삭제
		@DeleteMapping("/review/{reviewNo}")
		public boolean deleteReview(@PathVariable int reviewNo) {
			return reviewDao.delete(reviewNo);
		}

    // 배너 관리
    @PostMapping("/banner")
    public void addBanner(@ModelAttribute BannerDto bannerDto,@RequestParam(required = false) MultipartFile attach
    ) throws IOException {

        int bannerNo = bannerDao.sequence();
        bannerDto.setBannerNo(bannerNo);

        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            bannerDto.setBannerAttachmentNo(attachmentNo);
        }

        bannerDao.insert(bannerDto);
    }

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

    // 카테고리 관리
    @PostMapping("/category")
    public void addCategory(@RequestBody CategoryDto categoryDto) {
        int seq = (int) categoryDao.sequence();
        categoryDto.setCategoryNo(seq);

        categoryDao.insert(categoryDto);
    }
    @PutMapping("/category/{categoryNo}")
    public void editCategory(@PathVariable int categoryNo,
    		@RequestBody CategoryDto categoryDto
    ) {
        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }
        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }
    @PatchMapping("/category/{categoryNo}")
    public void editCategoryUnit(@PathVariable int categoryNo, 
    		@RequestBody CategoryDto categoryDto
    ) {
        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }

        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }

    @DeleteMapping("/category/{categoryNo}")
    public void deleteCategory(@PathVariable int categoryNo) {
        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }

        categoryDao.delete(categoryNo);
    }

    // 카테고리 이미지 관리
    @PostMapping("/category/image")
    public void addCategoryImage(@RequestParam int categoryNo, @RequestParam MultipartFile attach
    ) throws IOException {

        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }
        categoryDao.deleteCategoryImage(categoryNo);
        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            categoryDao.insertCategoryImage(categoryNo, attachmentNo);
        }
    }
    @DeleteMapping("/category/image/{categoryNo}")
    public void deleteCategoryImage(@PathVariable int categoryNo) {

        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }
        categoryDao.deleteCategoryImage(categoryNo);
    }

}
