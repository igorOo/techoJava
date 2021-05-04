package ru.technoteinfo.site.controllers.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Category;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.services.NewsCategoryService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/news")
public class NewsCategoryController {
    @Autowired
    private NewsCategoryService newsCategoryService;

    @RequestMapping(value = "/{translit}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listCategory(
            @PathVariable("translit") String translit,
            @RequestParam(value = "author", required = false) boolean author
    ){
        List<Category> list = newsCategoryService.getCategoryList(translit);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
