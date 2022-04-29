package com.technews.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private Integer id;
    private String commentText;
    private Integer userId;
    private Integer postId;
}
