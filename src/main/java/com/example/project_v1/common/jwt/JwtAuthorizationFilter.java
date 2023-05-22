package com.example.project_v1.common.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.project_v1.common.Service.SecurityService;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityService securityService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SecurityService securityService) {
        super(authenticationManager);
        this.securityService = securityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwtHeader = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);

        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){     // 토큰이 없거나, 이상한 형태로 들어왔을 경우
            chain.doFilter(request,response);
        } else {
            try{
                securityService.checkAccessToken(request);
                chain.doFilter(request, response);
            } catch (TokenExpiredException e){
                if(request.getRequestURI().equals("/reissue")){
                    try{
                        securityService.checkRefreshToken(request, response);
                    } catch (TokenExpiredException e2){
                        securityService.setRes(response, HttpStatus.UNAUTHORIZED, "refresh 토큰이 만료되었습니다.");
                    }
                } else{
                    securityService.setRes(response, HttpStatus.UNAUTHORIZED, "access 토큰이 만료되었습니다.");
                }
            } catch (Exception e){
                System.out.println("CustomAuthorizationFilter : JWT 토큰이 잘못되었습니다. message : " + e.getMessage());
            }
        }

    }

}
