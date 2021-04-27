package ru.technoteinfo.site.controllers.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Comments;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.pojo.CommentRequest;
import ru.technoteinfo.site.services.CommentsService;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        final int countPerPage = 5;
        if (page > 0){
            page -= 1;
        }
        Pageable pageable = PageRequest.of(page, countPerPage, Sort.by("createdAt").descending());

        int count = commentsService.getCountCommentsByPostId(postId);
        int currentPage = pageable.getPageNumber()+1;
        int lastPage = (int) Math.ceil((double)count/countPerPage);
        HashMap<String, Integer> pages = new HashMap<>();
        pages.put("currentPage", currentPage);
        pages.put("lastPage", lastPage);

        HashMap<String, Object> result = new HashMap<>();
        result.put("comments", commentsService.getListComments(postId, pageable));
        result.put("count_comments", count);
        result.put("pages", pages);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/{post_id}/addcomment")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addComment(
            @PathVariable(value = "post_id") String postId,
            @RequestParam(value = "comment") String comment,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "reply", required = false) String reply,
            HttpServletRequest request
    ){
        CommentRequest commentRequest = new CommentRequest(postId, comment, email, reply);
        Comments result = commentsService.saveComment(commentRequest, request);
        if (result != null){
            HashMap<String, Comments> id = new HashMap<>();
            id.put("result", result);
            return new ResponseEntity(id, HttpStatus.CREATED);
        }else{
           return new ResponseEntity("Не удалось добавить комментарий", HttpStatus.NOT_MODIFIED);
        }
    }
}

