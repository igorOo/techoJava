package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Comments;
import ru.technoteinfo.site.repositories.CommentsRepository;

import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    public List<Comments> getListComments(String postId, Pageable pageable){
        List<Comments> comments = commentsRepository.findByPostIdIgnoreCase(postId, pageable);
        for (Comments comment: comments){
            comment.setAvatar(comment.getCreatedBy().getAvatar());
        }
        return comments;
    }

    public Integer getCountCommentsByPostId(String postId){
        return commentsRepository.countByPostId(postId);
    }
}
