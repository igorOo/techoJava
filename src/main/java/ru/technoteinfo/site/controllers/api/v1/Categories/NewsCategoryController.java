package ru.technoteinfo.site.controllers.api.v1.Categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Category;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.services.NewsCategoryService;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/news/category")
public class NewsCategoryController {
    @Autowired
    private NewsCategoryService newsCategoryService;

    @RequestMapping(value = "/{translit}",
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

        int count = newsCategoryService.getCountCommentsByPostId(translit);
        int currentPage = pageable.getPageNumber();
        int lastPage = (int) Math.ceil((double)count/pageSize);
        HashMap<String, Integer> pages = new HashMap<>();
        pages.put("currentPage", currentPage);
        pages.put("lastPage", lastPage);

        List<TopPost> list = newsCategoryService.getCategoryList(translit, page, pageSize);
        result.put("posts", list);
        result.put("pages", pages);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
