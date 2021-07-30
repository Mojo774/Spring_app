package com.example.demo.repository;

import com.example.demo.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    // Название метода пишится по правилам из спринг
    List<Post> findByTitle(String title);
}
