package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.technoteinfo.site.services.PostsService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
public class MainController {
    @Autowired
    private PostsService postsService;

    @GetMapping("/mainpage")
    public HashMap<String, Object> mainpage(@RequestParam(value = "author", required = false) boolean author){
        HashMap<String, Object> result = new LinkedHashMap<>();
        result.put("tops", postsService.findTopPosts(author));
        result.put("gadgets", postsService.findGadgetPosts(author, "gadgets"));
        return result;
    }

}
