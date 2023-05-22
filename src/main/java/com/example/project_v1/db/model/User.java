package com.example.project_v1.db.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    private String id;
    private String password;
    private String name;
    private String role;

    @Builder
    public User(String id, String password, String name, String role) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = role;
    }


}