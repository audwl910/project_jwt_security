package com.example.project_v1.api.resoponse;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoRes {

    String id;
    String name;

    @Builder
    public UserInfoRes(String id, String name){
        this.id = id;
        this.name = name;
    }

}
