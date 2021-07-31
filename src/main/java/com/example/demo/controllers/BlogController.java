package com.example.demo.controllers;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public String blogMain(@RequestParam(required = false, defaultValue = "") String filter, Model model) {

        Iterable<Post> posts;

        if (filter != null && !filter.isEmpty()) {
            posts = postRepository.findByTitle(filter);

        } else {
            posts = postRepository.findAll();
        }
        model.addAttribute("posts", posts);
        model.addAttribute("filter", filter);


        return "blog-main";
    }

    @GetMapping("/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    // Параметры - это атрибуты, которые пользователь ввел. Названия такие же как в html
    @PostMapping("/add")
    public String blogPostAdd(
            @AuthenticationPrincipal User user,
            @RequestParam String title,
            @RequestParam String anons,
            @RequestParam String fullText, Model model) {


        Post post = new Post(title, anons, fullText, user);
        postRepository.save(post);
        return "redirect:/blog";
    }


    @GetMapping("/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }

        Optional<Post> post = postRepository.findById(id);
        // С list проще работать чем с optinal
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        return "blog-details";
    }

    @GetMapping("/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }

        Optional<Post> post = postRepository.findById(id);
        // С list проще работать чем с optinal
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        return "blog-edit";
    }


    @PostMapping("/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String fullText, Model model) {

        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(fullText);

        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {

        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);


        return "redirect:/blog";
    }
}
