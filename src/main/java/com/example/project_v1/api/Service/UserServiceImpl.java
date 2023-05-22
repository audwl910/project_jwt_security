package com.example.project_v1.api.Service;

import com.example.project_v1.api.request.UserPostReq;
import com.example.project_v1.api.resoponse.UserInfoRes;
import com.example.project_v1.db.model.User;
import com.example.project_v1.db.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    @Override
    public void registerUser(UserPostReq userPostReq) {

        User user = User.builder()
                .name(userPostReq.getName())
                .id(userPostReq.getId())
                .password(passwordEncoder.encode(userPostReq.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);

    }

    // 회원 정보 조회
    @Override
    public UserInfoRes infoUser(User user) {
        return UserInfoRes.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}
