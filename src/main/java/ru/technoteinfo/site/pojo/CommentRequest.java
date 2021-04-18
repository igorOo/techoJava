package ru.technoteinfo.site.pojo;

import lombok.Data;

@Data
public class CommentRequest {
    private String postId;
    private String text;
    private String email;
    private String reply;
}
