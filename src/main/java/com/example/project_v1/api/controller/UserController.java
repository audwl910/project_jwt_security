package com.example.project_v1.api.controller;

import com.example.project_v1.api.Service.UserService;
import com.example.project_v1.api.request.UserPostReq;
import com.example.project_v1.api.resoponse.UserInfoRes;
import com.example.project_v1.common.auth.PrincipalDetails;
import com.example.project_v1.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping()
    public ResponseEntity<?> register(@RequestBody UserPostReq userPostReq){
        userService.registerUser(userPostReq);
        return ResponseEntity.status(200).body("회원가입에 성공하였습니다.");
    }

    // 정보조회
    @GetMapping("/infoUser")
    public ResponseEntity<?> info(Authentication authentication){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        UserInfoRes userInfoRes = userService.infoUser(user);
        return ResponseEntity.status(200).body(userInfoRes);
    }

}