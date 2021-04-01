package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Comments;
import ru.technoteinfo.site.services.CommentsService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("/{post_id}/{page}")
    public List<Comments> getCommentsByPost(
            @RequestParam("post_id") Long postId,
            @RequestParam(value = "page", required = false) Integer page
    ){
        if (page == 0){
            page = 1;
        }
        List<Comments> list = commentsService.getListComments(postId, PageRequest.of(page, 20));
        return list;
    }
}

