package com.example.demo.service;


import com.example.demo.models.Grade;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

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

    public Page<Post> getPostsByFilter(
            Pageable pageable,
            String filter,
            User author) {

        boolean isAuthorNull = author == null;
        boolean isFilterNull = filter == null || filter.isEmpty();

        if (!isAuthorNull) {

            if (!isFilterNull)
                return postRepository.findByTitleAndUser(pageable, filter, author);
            else
                return getPostsByAuthor(pageable, author);
        }

        if (!isFilterNull)
            return postRepository.findByTitle(pageable, filter);

        return postRepository.findAll(pageable);
    }

    public Page<Post> getPostsByAuthor(Pageable pageable, User author) {
        return postRepository.findByUser(pageable, author);
    }

    public void ratePost(User user, Post post, Grade grade) {
        Set<User> set = post.getSetGrade(grade);

        if (!set.contains(user)) {
            resetGrade(post, user);

            set.add(user);

        } else {
            resetGrade(post, user);
        }

        postRepository.save(post);
    }

    // Отметить какая из оценок активирована у пользователя
    public int getGrade(Post post, User user) {
        if (post.getLikes().contains(user))
            return 1;
        if (post.getOks().contains(user))
            return 2;
        if (post.getDislikes().contains(user))
            return 3;
        return 0;
    }

    // Проверка доступа пользователя к редактированию поста
    public boolean access(Post post, User user) {
        if (post.getAuthor().equals(user) || user.isAdmin())
            return true;

        return false;
    }
}
