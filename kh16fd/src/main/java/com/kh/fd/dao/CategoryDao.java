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

    public List<CategoryDto> selectList() {
    	return sqlSession.selectList("category.list");
    }
    
    public void insert(CategoryDto dto) {
        sqlSession.insert("category.insert", dto);
    }

    public CategoryDto selectOne(int categoryNo) {
        return sqlSession.selectOne("category.detail", categoryNo);
    }

    public boolean update(CategoryDto dto) {
        return sqlSession.update("category.update", dto) > 0;
    }

    public boolean delete(int categoryNo) {
        return sqlSession.delete("category.delete", categoryNo) > 0;
    }
}