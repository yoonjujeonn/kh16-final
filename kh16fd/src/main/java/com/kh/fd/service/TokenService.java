package com.kh.fd.service;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.fd.configuration.JwtProperties;
import com.kh.fd.dao.MemberTokenDao;
import com.kh.fd.dto.MemberDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;



//JWT 토큰의 생성 및 검사 등을 수행하는 서비스
@Service
public class TokenService {
	@Autowired
	private JwtProperties jwtProperties;
	
	@Autowired
	private MemberTokenDao memberTokenDao;
	
	
	//아직 오류를 고치지 못함
	/**
	 * 로그인한 사용자에게 향후 접근을 위한 액세스 토큰을 만드는 기능
	 */
//	public String generateAccessToken(MemberDto memberDto) {
//		String keyStr = jwtProperties.getKeyStr();//설정파일에 있는 keyStr값
//		SecretKey key = Keys.hmacShaKeyFor(keyStr.getBytes(StandardCharsets.UTF_8));
//
//		//만료시간 설정
//		Calendar c = Calendar.getInstance();
//		Date now = c.getTime();//현재 시각
//		c.add(Calendar.MINUTE, jwtProperties.getExpiration());
//		Date expire = c.getTime();//만료 시각
//		
//		//JWT 토큰 생성
//		return Jwts.builder()
//				.signWith(key)//토큰 해독에 사용할 키 설정
//				.expiration(expire)//토큰의 만료 시각 설정
//				.issuedAt(now)//발행 시각 설정
//				.issuer(jwtProperties.getIssuer())//발행자 (위변조 방지용)
//				.claim("loginId", MemberDto.getMemberId())//정보 추가(key,value)
//				.claim("loginLevel", MemberDto.getMemberLevel())//정보 추가(key,value)
//			.compact();
//	}
	
}
