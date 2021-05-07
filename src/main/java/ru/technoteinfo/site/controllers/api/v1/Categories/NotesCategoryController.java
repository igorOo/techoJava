package ru.technoteinfo.site.controllers.api.v1.Categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.services.PostsCategoryService;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/notes")
public class NotesCategoryController {
    @Autowired
    private PostsCategoryService notesCategoryService;


    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listCategory(
            @RequestParam(name = "page", required = false) Integer page
    ){
        HashMap<String, Object> result = new HashMap<>();
        final int pageSize = 9;
        if (page == null){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        int count = notesCategoryService.getCountPostsByType(2L);
        int currentPage = pageable.getPageNumber();
        int lastPage = (int) Math.ceil((double)count/pageSize);
        HashMap<String, Integer> pages = new HashMap<>();
        pages.put("currentPage", currentPage);
        pages.put("lastPage", lastPage);

        List<TopPost> list = notesCategoryService.getPostsByType(2L,false, false, page, pageSize);
        result.put("posts", list);
        result.put("pages", pages);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/category/{translit}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listCategory(
            @PathVariable("translit") String translit,
            @RequestParam(name = "page", required = false) Integer page
    ){
        HashMap<String, Object> result = new HashMap<>();
        final int pageSize = 9;
        if (page == null){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        int count = notesCategoryService.getCountPostsInCategory(translit);
        int currentPage = pageable.getPageNumber();
        int lastPage = (int) Math.ceil((double)count/pageSize);
        HashMap<String, Integer> pages = new HashMap<>();
        pages.put("currentPage", currentPage);
        pages.put("lastPage", lastPage);

        List<TopPost> list = notesCategoryService.getCategoryList(translit, 2L, page, pageSize);
        result.put("posts", list);
        result.put("pages", pages);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
