package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Comments;
import ru.technoteinfo.site.pojo.CommentRequest;
import ru.technoteinfo.site.repositories.CommentsRepository;
import ru.technoteinfo.site.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

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

    public void saveComment(CommentRequest commentRequest, HttpServletRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Comments comment = new Comments();
        comment.setPostId(commentRequest.getPostId());
        comment.setFrom(auth.getName());
        comment.setText(commentRequest.getText());
        comment.setDeleted(0);
        comment.setIpAddress(request.getRemoteAddr());
        comment.setCreatedBy(userRepository.findByName(auth.getName()));
        comment.setUpdatedBy(userRepository.findByName(auth.getName()));
        commentsRepository.save(comment);
    }
}
