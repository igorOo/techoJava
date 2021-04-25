package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.Comments;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.pojo.CommentRequest;
import ru.technoteinfo.site.repositories.CommentsRepository;
import ru.technoteinfo.site.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Comments> getListComments(String postId, Pageable pageable){
        List<Comments> comments = commentsRepository.findByPostIdIgnoreCaseOrderByCreatedAtAsc(postId, pageable);
        for (Comments comment: comments){
            if (comment.getCreatedBy() != null){
                comment.setAvatar(comment.getCreatedBy().getAvatar());
            }
            if (comment.getCreatedBy() != null){
                comment.setUser_id(comment.getCreatedBy().getId());
            }
        }
        return comments;
    }

    public Integer getCountCommentsByPostId(String postId){
        return commentsRepository.countByPostId(postId);
    }

    public Number saveComment(CommentRequest commentRequest, HttpServletRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Comments comment = new Comments();
        comment.setPostId(commentRequest.getPostId());
        comment.setFrom(auth.getName());
        comment.setText(commentRequest.getText());
        comment.setDeleted(0);
        comment.setIpAddress(request.getRemoteAddr());
        comment.setCreatedBy(userRepository.findByName(auth.getName()));
        comment.setUpdatedBy(userRepository.findByName(auth.getName()));
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());
        Comments res = commentsRepository.save(comment);
        if (res.getId() != null){
            return res.getId();
        }else {
            return  -1;
        }
    }
}
