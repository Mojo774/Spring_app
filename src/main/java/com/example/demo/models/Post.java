package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotBlank (message = "Please fill the message")
    @Length(max = 300, message = "Title too long")
    private String title;

    @NotBlank (message = "Please fill the message")
    @Length(max = 300, message = "Anons too long")
    private String anons;

    @NotBlank (message = "Please fill the message")
    @Length(max = 2048, message = "Message too long (more than 2kB)")
    private String fullText;

    private int views;
    private int viewsPerMonth;
    private int viewsPerWeek;

    private int likes;
    private int dislike;
    private int ok;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    // В модели обязательно должен быть пустой конструктор
    public Post(){}

    public Post(String title, String anons, String fullText, User user) {
        this.title = title;
        this.anons = anons;
        this.fullText = fullText;
        this.author = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getViewsPerMonth() {
        return viewsPerMonth;
    }

    public void setViewsPerMonth(int viewsPerMonth) {
        this.viewsPerMonth = viewsPerMonth;
    }

    public int getViewsPerWeek() {
        return viewsPerWeek;
    }

    public void setViewsPerWeek(int viewsPerWeek) {
        this.viewsPerWeek = viewsPerWeek;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int like) {
        this.likes = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }
}
