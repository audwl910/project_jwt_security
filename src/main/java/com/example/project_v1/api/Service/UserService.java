package com.example.project_v1.api.Service;

import com.example.project_v1.api.request.UserPostReq;
import com.example.project_v1.api.resoponse.UserInfoRes;
import com.example.project_v1.db.model.User;

public interface UserService {
    void registerUser(UserPostReq userPostReq);
    UserInfoRes infoUser(User user);
}
