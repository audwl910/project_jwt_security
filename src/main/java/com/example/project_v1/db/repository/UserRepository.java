package com.example.project_v1.db.repository;

import com.example.project_v1.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(String id);

    List<User> findAll();

    List<User> findAllByNameContainingIgnoreCase(String keyword);

}
