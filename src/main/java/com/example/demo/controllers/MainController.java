package com.example.demo.controllers;

import com.example.demo.models.Post;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        Page<Post> posts = postService.findAll(
                PageRequest.of(0, 9).withSort(Sort.Direction.DESC, "views")
        );
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {

        return "about";
    }

    @GetMapping("/start")
    public String start(Model model) {

        return "home-start";
    }

}
