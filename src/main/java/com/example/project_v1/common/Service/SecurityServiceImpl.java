package com.example.project_v1.common.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.project_v1.common.auth.PrincipalDetails;
import com.example.project_v1.common.jwt.JwtProperties;
import com.example.project_v1.db.model.RefreshToken;
import com.example.project_v1.db.model.User;
import com.example.project_v1.db.repository.RefreshTokenRepository;
import com.example.project_v1.db.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class SecurityServiceImpl implements SecurityService{

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public SecurityServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    // 엑세스 토큰 발급해주기
    @Override
    public String createAccessToken(User user){

        return JWT.create()
                .withSubject(user.getId())      // 토큰 이름
                .withExpiresAt(new Date(System.currentTimeMillis()+(JwtProperties.ACCESS_EXPIRATION_TIME)))
                .withClaim("num", user.getNum())
                .withClaim("id", user.getId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    // 리프레시 토큰 발급해주기
    @Override
    public String createRefreshToken(User user){
        String refreshJwtToken = JWT.create()
                .withSubject(user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.REFRESH_EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        RefreshToken refreshToken = RefreshToken.builder()
                .id(user.getId())
                .refreshToken(refreshJwtToken)
                .build();
        refreshTokenRepository.save(refreshToken);

        return refreshJwtToken;
    }

    // 엑세스 토큰 검증
    @Override
    public void checkAccessToken(HttpServletRequest request){
        String accessJwtToken = request.getHeader(JwtProperties.ACCESS_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,"");
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(accessJwtToken).getClaim("id").asString(); // 토큰 검증
        PrincipalDetails principalDetails = userRepository.findById(username)
                .map(PrincipalDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("존재하지 않는 사용자 입니다."));
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    // 리프레시 토큰 검증
    @Override
    public void checkRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshJwtToken = request.getHeader(JwtProperties.REFRESH_HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,"");
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(refreshJwtToken).getClaim("id").asString();

        RefreshToken refreshToken = refreshTokenRepository.findById(username)
                .orElseThrow(()->new UsernameNotFoundException("존재하지 않는 사용자 입니다."));

        User user = userRepository.findById(username)
                .orElseThrow(()->new UsernameNotFoundException("존재하지 않는 사용자 입니다."));

        if(refreshToken.getRefreshToken().equals(refreshJwtToken)){
            String accessJwtToken = createAccessToken(user);
            response.addHeader(JwtProperties.ACCESS_HEADER_STRING, JwtProperties.TOKEN_PREFIX+accessJwtToken);
            setRes(response, HttpStatus.OK, "엑세스 토큰이 재발급 되었습니다.");
        } else {
            setRes(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
        }
    }

    // 응답
    @Override
    public void setRes(HttpServletResponse response, HttpStatus status, String txt) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(txt);
    }


}
