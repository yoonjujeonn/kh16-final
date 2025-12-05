package com.kh.fd.configuration;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class TokenRenewalInterceptor {
    private final ObjectMapper objectMapper;
	
	TokenRenewalInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
