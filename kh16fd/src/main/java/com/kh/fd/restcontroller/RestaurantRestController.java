package com.kh.fd.restcontroller;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.fd.dao.AttachmentDao;
import com.kh.fd.dao.RestaurantDao;
import com.kh.fd.dao.RestaurantHolidayDao;
import com.kh.fd.dto.AttachmentDto;
import com.kh.fd.dto.RestaurantDto;
import com.kh.fd.dto.RestaurantHolidayDto;
import com.kh.fd.service.AttachmentService;
import com.kh.fd.service.RestaurantService;
import com.kh.fd.service.TokenService;
import com.kh.fd.vo.PageVO;
import com.kh.fd.vo.RestaurantListPagingVO;
import com.kh.fd.vo.RestaurantListVO;
import com.kh.fd.vo.RestaurantRegisterVO;
import com.kh.fd.vo.SearchVO;
import com.kh.fd.vo.TokenVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@Tag(name = "식당 관리 컨트롤러")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/restaurant")
public class RestaurantRestController {
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RestaurantHolidayDao restaurantHolidayDao;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentDao attachmentDao;
	
	@PostMapping("/")
	public RestaurantDto add(@Valid @RequestBody RestaurantRegisterVO restaurantRegisterVO, @RequestHeader("Authorization") String bearerToken) {
	    TokenVO tokenVO = tokenService.parse(bearerToken);

	    restaurantRegisterVO.setOwnerId(tokenVO.getLoginId());

	    long restaurantId = restaurantService.createRestaurant(restaurantRegisterVO);
	    
	    return restaurantDao.selectOne(restaurantId);
	}
	
	@PostMapping("/search")
	public List<RestaurantListVO> search(@RequestBody SearchVO searchVO) {
	    return restaurantDao.searchList(searchVO);
	}
	
	@PostMapping("/holiday")
	public void add(@RequestBody List<RestaurantHolidayDto> holidays) {
		for(RestaurantHolidayDto holidayDto : holidays) {
			restaurantHolidayDao.insert(holidayDto);
		}
	}
	
	@GetMapping("/{restaurantId}")
	public RestaurantDto detail(@PathVariable long restaurantId) {
		RestaurantDto restaurantDto = restaurantDao.selectOne(restaurantId);
		
		return restaurantDto;
	}
	
	@GetMapping("/page/{page}")
	public RestaurantListPagingVO list(@PathVariable int page){
		PageVO pageVO = new PageVO();
		pageVO.setPage(page);
		pageVO.setDataCount(restaurantDao.listCount());
		
		List<RestaurantListVO> list = restaurantDao.selectListWithCategory(pageVO);
		
		return RestaurantListPagingVO.builder()
					.page(pageVO.getPage())
					.size(pageVO.getSize())
					.count(pageVO.getDataCount())
					.begin(pageVO.getBegin())
					.end(pageVO.getEnd())
					.last(pageVO.getPage() >= pageVO.getTotalPage())
					.list(list)
				.build();
	}
	
	@PostMapping("/image")
	public ResponseEntity<String> uploadImage(
	        @RequestParam("restaurantId") long restaurantId,
	        @RequestParam("attach") MultipartFile attach){
		if(attach.isEmpty()) {
			return ResponseEntity.badRequest().body("대표 이미지 설정은 필수입니다");
		}
		
		try {
			int attachmentNo = attachmentService.save(attach);
			restaurantDao.connect(restaurantId, attachmentNo);
			return ResponseEntity.ok("이미지가 정상적으로 등록되었습니다");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("업로드 실패: " + e.getMessage());
		}
		}
	
	@GetMapping("/image/{restaurantId}")
	public ResponseEntity<ByteArrayResource> getImage(@PathVariable long restaurantId){
		try {
	        // 식당 ID로 attachmentNo 조회
	        int attachmentNo = restaurantDao.findAttachment(restaurantId);
	        AttachmentDto attachmentDto = attachmentDao.selectOne(attachmentNo);
	        // 실제 파일 읽기
	        ByteArrayResource resource = attachmentService.load(attachmentNo);
	        
	        // MIME 타입에서 확장자 추출
	        String mimeType = attachmentDto.getAttachmentType(); // e.g., "image/png"
	        String ext = mimeType.substring(mimeType.indexOf("/") + 1); // "png"

	        // 동적 파일 이름
	        String fileName = "profileBy" + restaurantId + "." + ext;
	        
	        // MIME 타입, 파일 이름 설정
	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(attachmentDto.getAttachmentType())) // 또는 attachmentDto.getAttachmentType()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
	                .body(resource);

	    } catch (Exception e) {
	        // 기본 이미지 읽기
	        try {
	            ClassLoader classLoader = getClass().getClassLoader();
	            File file = new File(classLoader.getResource("static/images/no-image.png").getFile());
	            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));

	            return ResponseEntity.ok()
	                    .contentType(MediaType.IMAGE_PNG)
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"no-image.png\"")
	                    .body(resource);

	        } catch (Exception ex) {
	            // 기본 이미지도 못 읽으면 404
	            return ResponseEntity.notFound().build();
	        }
	    }
	}
	
}
