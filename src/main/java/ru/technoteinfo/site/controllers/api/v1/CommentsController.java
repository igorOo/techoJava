package ru.technoteinfo.site.controllers.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Comments;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.services.CommentsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @JsonView(JsonViewer.Public.class)
    @GetMapping("/{post_id}/{page}")
    public HashMap<String, Object> getCommentsByPost(
            @PathVariable("post_id") String postId,
            @PathVariable(value = "page", required = false) Integer page
    ){
        if (page > 0){
            page -= 1;
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("comments", commentsService.getListComments(postId, PageRequest.of(page, 20, Sort.by("createdAt").descending())));
        result.put("count_comments", commentsService.getCountCommentsByPostId(postId));
        return result;
    }
}

