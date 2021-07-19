package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/bookmarks")
public class BookmarkController {

    @RequestMapping(name = "/list")
    public ResponseEntity<?> getList(){

    }
}
