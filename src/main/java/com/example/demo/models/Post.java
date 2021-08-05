package com.example.demo.models;

import com.sun.istack.NotNull;
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

}
