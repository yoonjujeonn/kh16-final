package com.kh.fd.configuration;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;


//인터셉터 작성중
//이 인터셉터의 목적은 Bearer 토큰을 갱신처리하는것
//1. 만료임박
//2. 만료됨
@Service
public class TokenRenewalInterceptor {
    private final ObjectMapper objectMapper;
	
	TokenRenewalInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
