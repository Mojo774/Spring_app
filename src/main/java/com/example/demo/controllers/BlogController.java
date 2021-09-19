package com.example.demo.controllers;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private PostService postService;


    @GetMapping()
    public String blogMain(@RequestParam(required = false, defaultValue = "") String filter, Model model) {

        Iterable<Post> posts;

        if (filter != null && !filter.isEmpty()) {
            posts = postService.findByTitle(filter);

        } else {
            posts = postService.findAll();
        }


        model.addAttribute("posts", posts);
        model.addAttribute("filter", filter);


        return "blog-main";
    }

    @GetMapping(params = "clear")
    public String clear(Model model) {
        blogMain("", model);
        return "blog-main";
    }

    @GetMapping("/add")
    public String blogAdd(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "blog-add";
    }

    // Параметры - это атрибуты, которые пользователь ввел. Названия такие же как в html
    @PostMapping("/add")
    public String blogPostAdd(
            @AuthenticationPrincipal User user,
            @Valid Post post,
            BindingResult bindingResult, // Должен идти перед аргументом Model
            Model model) {

        model.addAttribute("post", post);

        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

        if (!errorsMap.isEmpty()) {


            model.mergeAttributes(errorsMap);

            return "blog-add";

        } else {
            post.setAuthor(user);


            postService.save(post);

            return "redirect:/blog";
        }

    }


    @GetMapping("/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        if (!postService.existsById(id)) {
            return "redirect:/blog";
        }

        Post post = postService.findById(id);
        postService.addView(post);

        model.addAttribute("post", post);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("currentUser", user);


        return "blog-details";
    }

    @GetMapping("/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        if (!postService.existsById(id)) {
            return "redirect:/blog";
        }

        Post post = postService.findById(id);

        model.addAttribute("post", post);

        return "blog-edit";
    }


    @PostMapping("/{id}/edit")
    public String blogPostUpdate(
            @PathVariable(value = "id") long id,
            @Valid Post post,
            BindingResult bindingResult,
            Model model) {

        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

        if (!errorsMap.isEmpty()) {
            model.mergeAttributes(errorsMap);

            return "blog-edit";

        } else {
            postService.update(id, post);

            return "redirect:/blog";
        }
    }

    @PostMapping("/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {

        Post post = postService.findById(id);

        postService.delete(post);


        return "redirect:/blog";
    }


}
