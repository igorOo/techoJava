package ru.technoteinfo.site.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequest {
    private String postId;
    private String text;
    private String email;
    private String reply;
}
