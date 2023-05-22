package com.example.project_v1.api.Service;

import com.example.project_v1.api.resoponse.UserInfoRes;
import com.example.project_v1.db.model.User;
import com.example.project_v1.db.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserInfoRes> userList() {
        List<UserInfoRes> res = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            UserInfoRes userInfoRes = UserInfoRes.builder()
                    .name(user.getName())
                    .id(user.getId())
                    .build();

            res.add(userInfoRes);
        }

        return res;
    }

    @Override
    public List<UserInfoRes> searchUserList(String keyword) {
        List<UserInfoRes> res = new ArrayList<>();
        List<User> list = userRepository.findAllByNameContainingIgnoreCase(keyword);
        for (User user : list) {
            UserInfoRes userInfoRes = UserInfoRes.builder()
                    .name(user.getName())
                    .id(user.getId())
                    .build();

            res.add(userInfoRes);
        }

        return res;
    }


}
