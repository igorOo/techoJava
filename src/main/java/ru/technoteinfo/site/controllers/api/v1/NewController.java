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

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/new")
public class NewController {
    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/{translit}",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViewer.ExtendedPublic.class)
    public Posts getPostByTranslit(
            @PathVariable("translit") String translit,
            @RequestParam(value = "author", required = false) boolean author
    ){
        Posts news = newsService.findNewByTranslit(translit, author, false);
        return news;
    }

    @RequestMapping(value = "/similar-posts/{category}")
    public List<TopPost> getSimilarPosts(
            @PathVariable("category") String category
    ){
        return newsService.findSimilarPosts(category);
    }
}
