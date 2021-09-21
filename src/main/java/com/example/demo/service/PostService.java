package com.example.demo.service;


import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Page<Post> findByTitle(Pageable pageable, String filter) {
        return postRepository.findByTitle(pageable, filter);
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
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

    public void like(long id) {
        Post post = findById(id);



        postRepository.save(post);

    }

    public Page<Post> getPostsByFilter(Pageable pageable, String filter) {
        if (filter != null && !filter.isEmpty()) {
            return postRepository.findByTitle(pageable, filter);

        } else {
            return postRepository.findAll(pageable);
        }
    }

    public Page<Post> getPostsByAuthor(Pageable pageable, User author) {
        return postRepository.findByUser(pageable, author);
    }
}
