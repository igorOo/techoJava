package ru.technoteinfo.site.controllers.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.services.NewsService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/new")
public class NewController {
    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/{translit}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViewer.DetailPost.class)
    public Posts getPostByTranslit(
            @PathVariable("translit") String translit,
            @RequestParam(value = "author", required = false) boolean author
    ){
        return newsService.findPostByTranslitAndType(translit, 1L, author, false);
    }

    @RequestMapping(value = "/get-other-posts/{category}")
    public HashMap<String, Object> getSimilarPosts(
            @PathVariable("category") String category//,
            //@PathVariable("post_id") Long postId
    ){
        HashMap<String, Object> result = new LinkedHashMap<>();
        result.put("similar-posts", newsService.findSimilarPosts(category, 1L));
        result.put("top-reader-posts", newsService.findTopReaderPosts());
        result.put("random-image-posts", newsService.findRandomImagePosts());
        return result;
    }
}
