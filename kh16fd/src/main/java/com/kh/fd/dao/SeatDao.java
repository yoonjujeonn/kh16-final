package com.kh.fd.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.SeatDto;
import com.kh.fd.error.TargetNotFoundException;
import com.kh.fd.vo.SeatListVO;

@Repository
public class SeatDao {
	@Autowired
	private SqlSession sqlSession;
	
	public SeatDto insert(SeatDto seatDto) {
		long sequence = sqlSession.selectOne("seat.sequence");
		seatDto.setSeatId(sequence);
		sqlSession.insert("seat.insert", seatDto);
		return seatDto;
	}
	
	public SeatDto selectOne(long seatId) {
		SeatDto seatDto = sqlSession.selectOne("seat.detail", seatId);
		if(seatDto == null) throw new TargetNotFoundException();
		return seatDto;
	}
	
	//식당별 전체 좌석
	public List<SeatDto> selectList(long seatRestaurantId){
		return sqlSession.selectList("seat.list", seatRestaurantId);
	}
	
	//미리보기용 좌석 리스트
	public List<SeatListVO> selectListByGroup(long seatRestaurantId){
		return sqlSession.selectList("seat.listByGroup", seatRestaurantId);
	}
}
