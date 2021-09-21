package com.example.demo.models.util;

import com.example.demo.models.User;

public abstract class PostHelper {
    public static String getAuthorName(User author){
        return author != null ? author.getUsername() : "<none>";
    }
}
