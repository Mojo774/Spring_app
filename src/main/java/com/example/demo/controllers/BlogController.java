package com.example.demo.controllers;

import com.example.demo.models.Grade;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private PostService postService;


    @GetMapping()
    public String blogMain(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
            ) {


        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Post> posts;

        posts = postService.getPostsByFilter(PageRequest.of(currentPage - 1, pageSize), filter);

        int totalPages = posts.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("filter", filter);

        int nextPage = currentPage < posts.getTotalPages() ? currentPage + 1: currentPage;
        int previousPage = currentPage > 1 ? currentPage - 1: currentPage;

        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);

        return "blog-main";
    }

    // todo починить
    @GetMapping(params = "clear")
    public String clear(Model model) {
       // blogMain("", model);
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
    public String blogDetails(
            @AuthenticationPrincipal User user,
            @PathVariable(value = "id") long id,
            Model model
    ) {
        if (!postService.existsById(id)) {
            return "redirect:/blog";
        }

        Post post = postService.findById(id);
        postService.addView(post);


        int grade = postService.getGrade(post, user);

        model.addAttribute("post", post);
        model.addAttribute("currentUser", user);
        model.addAttribute("grade", grade);

        model.addAttribute("likes", post.getLikes().size());
        model.addAttribute("dislikes", post.getDislikes().size());
        model.addAttribute("oks", post.getOks().size());

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

    // todo: заменить везде id на пост как тут (было как у дизлайка)
    @GetMapping("/{post}/{grade}")
    public String ratePost(
            @AuthenticationPrincipal User user,
            @PathVariable Post post,
            @PathVariable String grade,
            RedirectAttributes redirectAttributes,
            @RequestHeader (required = false) String referer,
            Model model
    ){

        postService.ratePost(user, post, Grade.valueOf(grade));

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:"+components.getPath();
    }



}
