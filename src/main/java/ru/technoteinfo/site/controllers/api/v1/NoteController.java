package ru.technoteinfo.site.controllers.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.services.NoteService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @RequestMapping(value = "/{translit}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViewer.DetailPost.class)
    public Posts getPostByTranslit(
            @PathVariable("translit") String translit,
            @RequestParam(value = "author", required = false) boolean author
    ){
        return noteService.findPostByTranslitAndType(translit, 2L, author, false);
    }

    @RequestMapping(value = "/get-other-posts/{category}/{post_id}")
    public ResponseEntity<?> getSimilarPosts(
            @PathVariable("category") String category,
            @PathVariable("post_id") Long postId
    ){
        HashMap<String, Object> result = new LinkedHashMap<>();
        result.put("similar-posts", noteService.findSimilarPosts(category));
        result.put("next-prev", noteService.getPrevAndNextPosts(postId, 2L));
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
