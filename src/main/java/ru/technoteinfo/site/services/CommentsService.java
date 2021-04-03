package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.technoteinfo.site.entities.Comments;
import ru.technoteinfo.site.repositories.CommentsRepo;

import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepo commentsRepo;

    public List<Comments> getListComments(String postId, Pageable pageable){
        return commentsRepo.findCommentsByPostIdIgnoreCase(postId, pageable);
    }
}
