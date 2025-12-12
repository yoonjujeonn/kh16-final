package com.kh.fd.aop;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kh.fd.error.NeedPermissionException;
import com.kh.fd.error.UnauthorizationException;
import com.kh.fd.service.TokenService;
import com.kh.fd.vo.TokenVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor{

    private final TokenService tokenService;

    public AdminInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new UnauthorizationException("로그인이 필요합니다");
        }
        
        TokenVO tokenVO = tokenService.parse(token);

        if (tokenVO.getLoginId() == null) {//로그인한지 확인
            throw new UnauthorizationException("로그인이 필요합니다");
        }

        if (!"관리자".equals(tokenVO.getLoginLevel())) {//로그인 한 회원의 등급이 관리자인지 확인
            throw new NeedPermissionException("관리자 권한이 없습니다");
        }

        return true;
    }
}
