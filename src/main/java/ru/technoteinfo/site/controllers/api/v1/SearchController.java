package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.impl.PostsRepositoryImpl;
import ru.technoteinfo.site.services.SearchService;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private PostsRepositoryImpl postsRepo;

    @GetMapping
    public ResponseEntity<?> search(
            @RequestParam("s") String searchStr,
            @RequestParam(name = "page", required = false) Integer page
    ){
        if (searchStr == ""){
            return new ResponseEntity("Строка запроса не может быть пустой", HttpStatus.BAD_REQUEST);
        }
        HashMap<String, Object> result = new HashMap<>();
        final int pageSize = 9;
        if (page == null){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        int count = postsRepo.getCountSearchResult(searchStr);
        int currentPage = pageable.getPageNumber();
        int lastPage = (int) Math.ceil((double)count/pageSize);
        HashMap<String, Integer> pages = new HashMap<>();
        pages.put("currentPage", currentPage);
        pages.put("lastPage", lastPage);

        List<TopPost> list = searchService.search(searchStr, pageSize, page);
        result.put("posts", list);
        result.put("pages", pages);


        return new ResponseEntity(result, HttpStatus.OK);
    }
}
