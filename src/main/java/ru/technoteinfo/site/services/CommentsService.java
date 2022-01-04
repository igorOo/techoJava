package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Comments;
import ru.technoteinfo.site.pojo.request.CommentRequest;
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

    @Autowired
    private CommonController common;

    public List<Comments> getListComments(String postId, Pageable pageable){
        List<Comments> comments = commentsRepository.findByPostIdIgnoreCaseOrderByCreatedAtAsc(postId, pageable);
        for (Comments comment: comments){
            if (comment.getCreatedBy() != null){
                String avatar = comment.getCreatedBy().getAvatar();
                if (avatar != null){
                    comment.setAvatar(common.getAvatarUrl(comment.getCreatedBy().getAvatar()));
                }
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

    public Comments saveComment(CommentRequest commentRequest, HttpServletRequest request){
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
            if (res.getCreatedBy() != null){
                String avatar = res.getCreatedBy().getAvatar();
                if (avatar != null){
                    res.setAvatar(common.getAvatarUrl(res.getCreatedBy().getAvatar()));
                }
            }
            return res;
        }else {
            return null;
        }
    }
}
