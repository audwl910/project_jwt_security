package com.example.project_v1.api.controller;

import com.example.project_v1.api.Service.AdminService;
import com.example.project_v1.api.resoponse.UserInfoRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/userList")
    public ResponseEntity<?> userList(){
        List<UserInfoRes> list = adminService.userList();
        return ResponseEntity.status(200).body(list);
    }

    @GetMapping("/searchUser/{keyword}")
    public ResponseEntity<?> searchUserList(@PathVariable String keyword){
        List<UserInfoRes> list = adminService.searchUserList(keyword);
        return ResponseEntity.status(200).body(list);
    }

}
