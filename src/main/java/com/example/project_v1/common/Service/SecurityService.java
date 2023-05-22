package com.example.project_v1.common.Service;

import com.example.project_v1.db.model.User;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SecurityService {

    String createAccessToken(User user);
    String createRefreshToken(User user);
    void checkAccessToken(HttpServletRequest request);
    void checkRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void setRes(HttpServletResponse response, HttpStatus status, String txt) throws IOException;
}
