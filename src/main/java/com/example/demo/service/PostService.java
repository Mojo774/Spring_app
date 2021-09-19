package com.example.demo.service;


import com.example.demo.models.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> findByTitle(String filter) {
        return postRepository.findByTitle(filter);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public Post findById(long id) {
        return postRepository.findById(id).orElseThrow();
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public boolean existsById(long id) {
        return postRepository.existsById(id);
    }

    public void addView(Post post) {
        post.setViews(post.getViews()+1);
        postRepository.save(post);
    }

    public void update(long id, Post post) {
        Post originalPost = findById(id);

        originalPost.setTitle(post.getTitle());
        originalPost.setAnons(post.getAnons());
        originalPost.setFullText(post.getFullText());

        postRepository.save(originalPost);
    }
}
