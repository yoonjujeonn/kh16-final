package com.kh.fd.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.CategoryDto;

@Repository
public class CategoryDao {

    @Autowired
    private SqlSession sqlSession;
    
    public long sequence() {
        return sqlSession.selectOne("category.sequence");
    }

    // 전체 카테고리 조회 
    public List<CategoryDto> selectList() {
        return sqlSession.selectList("category.list");
    }
    
    // 등록
    public void insert(CategoryDto categoryDto) {
        sqlSession.insert("category.insert", categoryDto);
    }

    // 단일 카테고리 조회
    public CategoryDto selectOne(int categoryNo) {
        return sqlSession.selectOne("category.detail", categoryNo);
    }

    // 수정
    public boolean updateUnit(CategoryDto categoryDto) {
        return sqlSession.update("category.updateUnit", categoryDto) > 0;
    }

    // 삭제
    public boolean delete(int categoryNo) {
        return sqlSession.delete("category.delete", categoryNo) > 0;
    }
    
    public List<CategoryDto> selectParentList() {
        return sqlSession.selectList("category.listParent");
    }

    public List<CategoryDto> selectChildList(int parentNo) {
        return sqlSession.selectList("category.listByParent", parentNo);
    }
    
}