package ru.technoteinfo.site.controllers.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.pojo.CommentRequest;
import ru.technoteinfo.site.services.CommentsService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @JsonView(JsonViewer.Public.class)
    @GetMapping("/{post_id}/{page}")
    public ResponseEntity<?> getCommentsByPost(
            @PathVariable("post_id") String postId,
            @PathVariable(value = "page", required = false) Integer page
    ){
        if (page > 0){
            page -= 1;
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("comments", commentsService.getListComments(postId, PageRequest.of(page, 20, Sort.by("createdAt").descending())));
        result.put("count_comments", commentsService.getCountCommentsByPostId(postId));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{post_id}/addcomment")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest comment, HttpServletRequest request){
        commentsService.saveComment(comment, request);
        return ResponseEntity.ok("true");
    }
}

