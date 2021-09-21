package com.example.demo.repository;

import com.example.demo.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    // Название метода пишится по правилам из спринг
    Page<Post> findByTitle(Pageable pageable, String title);

    Page<Post> findAll(Pageable pageable);
}
