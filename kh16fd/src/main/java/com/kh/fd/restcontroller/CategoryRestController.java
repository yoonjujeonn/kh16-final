package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kh.fd.dao.CategoryDao;
import com.kh.fd.dto.CategoryDto;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "카테고리 관리 컨트롤러")
@RestController
@RequestMapping("/category")
@CrossOrigin
public class CategoryRestController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TokenService tokenService;

    private void checkLogin(String bearerToken) {
        tokenService.parse(bearerToken); // 유효하지 않으면 예외 발생
    }

    // 등록 (로그인 필수)
    @Operation(
        description = "카테고리 등록",
        responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401"),
            @ApiResponse(responseCode = "500")
        }
    )
    @PostMapping("/")
    public void add(
        @RequestBody CategoryDto categoryDto,
        @RequestHeader(value="Authorization", required = false) String bearerToken
    ) {
        checkLogin(bearerToken);

        int seq = (int) categoryDao.sequence();
        categoryDto.setCategoryNo(seq);
        categoryDao.insert(categoryDto);
    }

    // 목록 (공개)
    @GetMapping("/")
    public List<CategoryDto> list() {
        return categoryDao.selectList();
    }

    // 단건 조회 (공개)
    @GetMapping("/{categoryNo}")
    public CategoryDto detail(@PathVariable int categoryNo) {
        CategoryDto categoryDto = categoryDao.selectOne(categoryNo);
        if (categoryDto == null)
            throw new TargetNotFoundException("존재하지 않는 카테고리 번호");
        return categoryDto;
    }

    // 삭제 (로그인 필수)
    @DeleteMapping("/{categoryNo}")
    public void delete(
        @PathVariable int categoryNo,
        @RequestHeader("Authorization") String bearerToken
    ) {
        checkLogin(bearerToken);

        if (categoryDao.selectOne(categoryNo) == null)
            throw new TargetNotFoundException("존재하지 않는 카테고리 번호");

        categoryDao.delete(categoryNo);
    }

    // 전체 수정 (로그인 필수)
    @PutMapping("/{categoryNo}")
    public void edit(
        @PathVariable int categoryNo,
        @RequestBody CategoryDto categoryDto,
        @RequestHeader("Authorization") String bearerToken
    ) {
        checkLogin(bearerToken);

        if (categoryDao.selectOne(categoryNo) == null)
            throw new TargetNotFoundException();

        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }

    // 부분 수정 (로그인 필수)
    @PatchMapping("/{categoryNo}")
    public void editUnit(
        @PathVariable int categoryNo,
        @RequestBody CategoryDto categoryDto,
        @RequestHeader("Authorization") String bearerToken
    ) {
        checkLogin(bearerToken);

        if (categoryDao.selectOne(categoryNo) == null)
            throw new TargetNotFoundException();

        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }
    
    @GetMapping("/parent")
    public List<CategoryDto> getParentList() {
        return categoryDao.selectParentList();
    }

    @GetMapping("/child/{parentNo}")
    public List<CategoryDto> getChildList(@PathVariable int parentNo) {
        return categoryDao.selectChildList(parentNo);
    }
    
}
