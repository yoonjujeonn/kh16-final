package com.kh.fd.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kh.fd.dao.CategoryDao;
import com.kh.fd.dto.CategoryDto;
import com.kh.fd.error.TargetNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "카테고리 관리 컨트롤러")
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryRestController {

    @Autowired
    private CategoryDao categoryDao;
    
    //등록
    @Operation(
        description = "카테고리 등록 기능",
        deprecated = false,
        responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "500")
        }
    )
    @PostMapping("/")
    public void add(@RequestBody CategoryDto categoryDto) {
        int seq = (int) categoryDao.sequence();
        categoryDto.setCategoryNo(seq);
        categoryDao.insert(categoryDto);
    }
    
    //목록
    @GetMapping("/")
    public List<CategoryDto> list() {
        return categoryDao.selectList();
    }

    //조회
    @GetMapping("/{categoryNo}")
    public CategoryDto detail(@PathVariable int categoryNo) {
        CategoryDto categoryDto = categoryDao.selectOne(categoryNo);
        if (categoryDto == null) throw new TargetNotFoundException("존재하지 않는 카테고리 번호");
        return categoryDto;
    }

    //삭제
    @DeleteMapping("/{categoryNo}")
    public void delete(@PathVariable int categoryNo) {
        CategoryDto categoryDto = categoryDao.selectOne(categoryNo);
        if (categoryDto == null) throw new TargetNotFoundException("존재하지 않는 카테고리 번호");
        categoryDao.delete(categoryNo);
    }

    //전체 수정 
    @PutMapping("/{categoryNo}")
    public void edit(@PathVariable int categoryNo, @RequestBody CategoryDto categoryDto) {
        if (categoryDao.selectOne(categoryNo) == null)
            throw new TargetNotFoundException();

        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }
    
    //부분 수정
 // 부분 수정 (PATCH) - updateUnit 사용
    @PatchMapping("/{categoryNo}")
    public void editUnit(@PathVariable int categoryNo, @RequestBody CategoryDto categoryDto) {
        if (categoryDao.selectOne(categoryNo) == null)
            throw new TargetNotFoundException();

        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }
}
