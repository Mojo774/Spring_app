package com.example.demo.models;

import com.example.demo.models.util.PostHelper;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotBlank (message = "Please fill the message")
    @Length(max = 40, message = "Title too long")
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

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = { @JoinColumn(name = "post_id")},
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_dislikes",
            joinColumns = { @JoinColumn(name = "post_id")},
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private Set<User> dislikes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_oks",
            joinColumns = { @JoinColumn(name = "post_id")},
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private Set<User> oks = new HashSet<>();


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

    public String getAuthorName(){
        return PostHelper.getAuthorName(author);
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

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public Set<User> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Set<User> dislikes) {
        this.dislikes = dislikes;
    }

    public Set<User> getOks() {
        return oks;
    }

    public void setOks(Set<User> oks) {
        this.oks = oks;
    }

    public Set<User> getSetGrade(Grade grade){
        switch (grade){
            case LIKE:
                return getLikes();
            case OK:
                return getOks();
            case DISLIKE:
                return getDislikes();
        }
        return null;
    }

}
