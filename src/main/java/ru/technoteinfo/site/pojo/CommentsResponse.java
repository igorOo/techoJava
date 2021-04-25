package ru.technoteinfo.site.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.technoteinfo.site.entities.Comments;

import java.util.Date;

@Data
@AllArgsConstructor
public class CommentsResponse {
    private Long id;
    private String entity;
    private String author;
    private String avatar;
    private String text;
    private Date created_at;
    private Date updated_at;
    private Long reply;
    private Long author_id;

    public CommentsResponse(Comments comments){
        this.id = comments.getId();
        this.entity = comments.getPostId();
        this.author = comments.getFrom();
        this.avatar = comments.getCreatedBy().getAvatar();
        this.text = comments.getText();
        this.created_at = comments.getCreatedAt();
        this.updated_at = comments.getUpdatedAt();
        this.reply = comments.getReplyComment().getId();
        this.author_id = comments.getCreatedBy().getId();
    }
}
