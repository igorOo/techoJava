package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.services.SearchService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(
            @RequestParam("s") String searchStr
    ){
        if (searchStr == ""){
            return new ResponseEntity("Строка запроса не может быть пустой", HttpStatus.BAD_REQUEST);
        }
        List<TopPost> result = searchService.search(searchStr);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
