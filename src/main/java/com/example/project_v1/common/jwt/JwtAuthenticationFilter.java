package com.example.project_v1.common.jwt;


import com.example.project_v1.common.Service.SecurityService;
import com.example.project_v1.api.request.LoginReq;
import com.example.project_v1.common.auth.PrincipalDetails;

import com.example.project_v1.db.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SecurityService securityService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityService securityService) {
        this.authenticationManager = authenticationManager;
        this.securityService = securityService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();

        try {
            LoginReq loginReq = om.readValue(request.getInputStream(), LoginReq.class);
            String id = loginReq.getId();
            String pwd = loginReq.getPassword();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, pwd);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인에 성공했을 경우
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        User user = ((PrincipalDetails) authResult.getPrincipal()).getUser();

        // access token 발급
        String accessJwtToken = securityService.createAccessToken(user);
        response.addHeader(JwtProperties.ACCESS_HEADER_STRING, JwtProperties.TOKEN_PREFIX+accessJwtToken);

        // refresh token 발급
        String refreshJwtToken = securityService.createRefreshToken(user);
        response.addHeader(JwtProperties.REFRESH_HEADER_STRING, JwtProperties.TOKEN_PREFIX+refreshJwtToken);

        securityService.setRes(response, HttpStatus.OK, "로그인 되었습니다.");
    }

    // 비밀번호가 틀렸을 경우
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException{

        securityService.setRes(response, HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 다시 확인해주시기 바랍니다.");
    }

}
