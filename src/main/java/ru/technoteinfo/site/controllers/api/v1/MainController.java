package ru.technoteinfo.site.controllers.api.v1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.services.MainService;
import ru.technoteinfo.site.services.PostsService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
public class MainController {
    @Autowired
    private PostsService postsService;

    @Autowired
    private MainService mainService;

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

    @GetMapping("/mainmenu")
    public ResponseEntity<?> mainMenu(){
        StringBuilder menuString = new StringBuilder();
        try{
            File file = new File(System.getProperty("user.dir")+"/config/mainMenu.json");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));
            while (reader.ready()){
                menuString.append(reader.readLine());
            }
            if (menuString.length() == 0){
                    throw new IOException("error parse json");
            }
        }catch (IOException e){
            menuString.append(mainService.createMenu());
        }
        return new ResponseEntity<>(menuString, HttpStatus.OK);
    }
}
