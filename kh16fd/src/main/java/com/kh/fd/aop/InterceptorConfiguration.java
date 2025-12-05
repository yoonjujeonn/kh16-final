package com.kh.fd.aop;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.kh.fd.configuration.TokenRenewalInterceptor;




@Configuration
public class InterceptorConfiguration {
//	@Autowired
//	private TokenRenewalInterceptor tokenRenewalInterceptor;	
	
	//현재 토큰 기능은 구현하지 않았습니다
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		
//		registry.addInterceptor(tokenRenewalInterceptor)
//		.addPathPatterns("/**")
//		.excludePathPatterns(
//				"/account/refresh", //이미 만료되서 갱신하므로 갱신대상 아님
//				"/account/join", //비회원 페이지들은 갱신대상아님
//				"/account/login", //비회원 페이지들은 갱신대상아님
//				"/account/logout", //토큰을 만료시켜야 하므로 갱신대상 아님
//				"/cert/**", //인증은 비회원이 사용하는 기능
//				"/ws", //웹소켓 접속 주소
//				"/websocket/**" //웹소켓 페이지
//		);
//		
//	}
	
}
