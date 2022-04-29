package com.technews.model;

import java.io.Serializable;

public class Post implements Serializable {
    private Integer id;
    private String title;
    private String postUrl;
    private String userName;
    private int voteCount;
    private Integer userId;
    private Date postedAt = new Date();
    private Date updatedAt = new Date();
    private List<Comment> comments;
}
