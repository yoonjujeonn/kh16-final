package com.kh.fd.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.AttachmentDto;
import com.kh.fd.dto.RestaurantDto;

@Repository
public class AttachmentDao {

    @Autowired
    private SqlSession sqlSession;  

    public int sequence() {
        return sqlSession.selectOne("attachment.sequence");
    }
    public void insert(AttachmentDto dto) {

        Integer restaurantNo = dto.getRestaurantNo() == 0 ? null : dto.getRestaurantNo();
        Integer bannerNo = dto.getBannerNo() == 0 ? null : dto.getBannerNo();
        Integer categoryNo = dto.getCategoryNo() == 0 ? null : dto.getCategoryNo();

        // 둘 중 두 개가 동시에 들어가면 안 됨
        if (restaurantNo != null && bannerNo != null) {
            throw new IllegalArgumentException("첨부파일은 식당 또는 배너 중 하나에만 연결될 수 있습니다.");
        }

        dto.setRestaurantNo(restaurantNo);
        dto.setBannerNo(bannerNo);

        sqlSession.insert("attachment.insert", dto);
    }

    public AttachmentDto selectOne(int attachmentNo) {
        return sqlSession.selectOne("attachment.selectOne", attachmentNo);
    }

    public boolean delete(int attachmentNo) {
        return sqlSession.delete("attachment.delete", attachmentNo) > 0;
    }

    // ------------------------
    // 식당 관련 기능
    // ------------------------

    public List<AttachmentDto> selectListByRestaurantNo(int restaurantNo) {
        return sqlSession.selectList("attachment.selectListByRestaurantNo", restaurantNo);
    }

    public boolean updateRestaurantNo(int attachmentNo, int restaurantNo) {
        AttachmentDto dto = new AttachmentDto();
        dto.setAttachmentNo(attachmentNo);
        dto.setRestaurantNo(restaurantNo);

        return sqlSession.update("attachment.updateRestaurantNo", dto) > 0;
    }

    // ------------------------
    // 배너 관련 기능
    // ------------------------

    public List<AttachmentDto> selectListByBannerNo(int bannerNo) {
        return sqlSession.selectList("attachment.selectListByBannerNo", bannerNo);
    }

    public boolean updateBannerNo(int attachmentNo, int bannerNo) {
        AttachmentDto dto = new AttachmentDto();
        dto.setAttachmentNo(attachmentNo);
        dto.setBannerNo(bannerNo);

        return sqlSession.update("attachment.updateBannerNo", dto) > 0;
    }
}
