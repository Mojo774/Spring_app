package com.example.demo.service;


import com.example.demo.models.Grade;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

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
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }

    public void update(long id, Post post) {
        Post originalPost = findById(id);

        originalPost.setTitle(post.getTitle());
        originalPost.setAnons(post.getAnons());
        originalPost.setFullText(post.getFullText());

        postRepository.save(originalPost);
    }

    // todo: сделать номарльно
    public void like(User user, long id, Grade grade) {
        Post post = findById(id);
        resetGrade(post, user);

        Set<User> set;
        switch (grade){
            case OK:
                set = post.getOks();
                if (!set.contains(user)) {
                    set.add(user);
                    post.setOks(set);
                } else {
                    resetGrade(post, user);
                }
                break;
            case LIKE:
                set = post.getLikes();
                if (!set.contains(user)) {
                    set.add(user);
                    post.setLikes(set);
                } else {
                    resetGrade(post, user);
                }
                break;
            case DISLIKE:
                set = post.getDislikes();
                if (!set.contains(user)) {
                    set.add(user);
                    post.setDislikes(set);
                } else {
                    resetGrade(post, user);
                }
                break;
        }


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

    public int getGrade(Post post, User user) {
        if (post.getLikes().contains(user))
            return 1;
        if (post.getOks().contains(user))
            return 2;
        if (post.getDislikes().contains(user))
            return 3;
        return 0;
    }

    private void resetGrade(Post post, User user) {
        Set<User> set = post.getLikes();
        set.remove(user);
        post.setLikes(set);

        set = post.getDislikes();
        set.remove(user);
        post.setDislikes(set);

        set = post.getOks();
        set.remove(user);
        post.setOks(set);

        postRepository.save(post);
    }

}
