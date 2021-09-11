package com.example.demo.repository;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Название метода пишится по правилам из спринг
    User findByUsername(String username);

    User findByActivationCode(String code);

    User findByEmail(String email);

    User findById(long id);
}