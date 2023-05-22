package com.example.project_v1.api.Service;

import com.example.project_v1.api.resoponse.UserInfoRes;

import java.util.List;

public interface AdminService {
    List<UserInfoRes> userList();

    List<UserInfoRes> searchUserList(String keyword);
}
