package com.example.demo.models.dto;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.models.util.PostHelper;

import java.util.HashSet;
import java.util.Set;

public class PostDto {
    private Long id;
    private String title;
    private String anons;
    private String fullText;
    private User author;
    private int views;
    private int viewsPerMonth;
    private int viewsPerWeek;

    private long likes;
    private long dislikes;
    private long oks;
    private boolean meAppreciated;

    public PostDto(Post post, long likes, boolean meAppreciated) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.anons = post.getAnons();
        this.fullText = post.getFullText();
        this.author = post.getAuthor();
        this.views = post.getViews();
        this.viewsPerMonth = post.getViewsPerMonth();
        this.viewsPerWeek = post.getViewsPerWeek();

        this.likes = likes;
        //this.dislikes = dislikes;
        //this.oks = oks;
        this.meAppreciated = meAppreciated;
    }

    public String getAuthorName(){
        return PostHelper.getAuthorName(author);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAnons() {
        return anons;
    }

    public String getFullText() {
        return fullText;
    }

    public User getAuthor() {
        return author;
    }

    public int getViews() {
        return views;
    }

    public int getViewsPerMonth() {
        return viewsPerMonth;
    }

    public int getViewsPerWeek() {
        return viewsPerWeek;
    }

    public long getLikes() {
        return likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public long getOks() {
        return oks;
    }

    public boolean getMeAppreciated() {
        return meAppreciated;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", anons='" + anons + '\'' +
                ", fullText='" + fullText + '\'' +
                ", author=" + author +
                ", views=" + views +
                ", viewsPerMonth=" + viewsPerMonth +
                ", viewsPerWeek=" + viewsPerWeek +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", oks=" + oks +
                ", meAppreciated=" + meAppreciated +
                '}';
    }
}
