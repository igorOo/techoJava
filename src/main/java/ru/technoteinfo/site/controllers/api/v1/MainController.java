package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.technoteinfo.site.services.PostsService;

import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
public class MainController {
    private PostsService postsService;

    @Autowired
    public PostsService getPostsService() {
        return postsService;
    }

    @GetMapping("/mainpage")
    public HashMap<String, Object> mainpage(@RequestParam(value = "author", required = false) boolean author){
        HashMap<String, Object> result = new LinkedHashMap<>();
        result.put("tops", postsService.findTopPosts(author));
        return result;
    }
}
