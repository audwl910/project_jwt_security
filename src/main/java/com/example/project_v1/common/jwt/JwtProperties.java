package com.example.project_v1.common.jwt;

public interface JwtProperties {

    String SECRET = "암호"; // 우리 서버만 알고 있는 비밀값
    long ACCESS_EXPIRATION_TIME = 30000;  // 10분     // 토큰 만료 시간 -> 60000 -> 1분
    long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 5; // 5일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String ACCESS_HEADER_STRING = "access-token";
    String REFRESH_HEADER_STRING = "refresh-token";

}
