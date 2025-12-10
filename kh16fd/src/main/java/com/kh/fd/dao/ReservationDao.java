package com.kh.fd.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.ReservationDto;
import com.kh.fd.error.TargetNotFoundException;

@Repository
public class ReservationDao {
	@Autowired
	private SqlSession sqlSession;
	
	public boolean checkAvailableSlot(ReservationDto reservationDto) {
		int count = sqlSession.selectOne("reservation.checkSlot", reservationDto);
		return count == 0; //예약 가능
	}
	
	public ReservationDto insert(ReservationDto reservationDto) {
		long sequence = sqlSession.selectOne("reservation.sequence");
		
		reservationDto.setReservationId(sequence);
		
		sqlSession.insert("reservation.insert", reservationDto);
		
		return reservationDto;
	}
	
	public ReservationDto selectOne(long reservationId) {
		ReservationDto reservationDto = sqlSession.selectOne("reservation.detail", reservationId);
		if(reservationDto == null) throw new TargetNotFoundException();
		return reservationDto;
	}
}
