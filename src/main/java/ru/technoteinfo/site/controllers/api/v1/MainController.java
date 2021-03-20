package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.services.PostsService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
@CrossOrigin(origins = "*")
public class MainController {
    @Autowired
    private PostsService postsService;

    @GetMapping("/mainpage")
    public HashMap<String, Object> mainpage(
            @RequestParam(value = "author", required = false) boolean author,
            @RequestParam(value = "page", required = false) Integer page
    ){
        if (page == null || page < 1){
            page=1;
        }
        HashMap<String, Object> result = new LinkedHashMap<>();
        result.put("tops", postsService.findTopPosts(author));
        result.put("gadgets", postsService.findListPosts(author, false, "gadgets", 6));
        result.put("interandprogs", postsService.findTwoCategoryPosts(author, new String[] {"programs", "internet"}, 6));
        result.put("hardware", postsService.findHardwarePosts(author, "hardware"));
        result.put("articles", postsService.findArticles(page, author));
        result.put("games", postsService.findGamesPosts(author, "games"));
        result.put("worldcar", postsService.findTwoCategoryPosts(author, new String[] {"auto", "in-world"}, 3));
        result.put("another", postsService.findListPosts(author, true, "any", 6));
        return result;
    }

}
