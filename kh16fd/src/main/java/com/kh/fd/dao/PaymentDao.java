package com.kh.fd.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.fd.dto.PaymentDto;

@Repository
public class PaymentDao {
	@Autowired
	private SqlSession sqlSession;
	
	public long sequence() {
		return sqlSession.selectOne("payment.sequence");
	}
	
	public void insert(PaymentDto paymentDto) {
		sqlSession.insert("payment.insert",paymentDto);
	}
	
//	public List<PaymentDto> selectList(TokenVO tokenVO){
//		return sqlSession.selectList("payment.listByOwner",tokenVO);
//	}
	
	public PaymentDto selectOne(long paymentNo) {
		return sqlSession.selectOne("payment.detail",paymentNo);
	}
	
	public boolean cancel(long paymentNo) {
		return sqlSession.update("payment.cancel", paymentNo)>0;
		
	}
}
