package ru.technoteinfo.site.controllers.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.services.NoteService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @RequestMapping(value = "/{translit}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViewer.DetailPost.class)
    public Posts getPostByTranslit(
            @PathVariable("translit") String translit,
            @RequestParam(value = "author", required = false) boolean author
    ){
        return noteService.findPostByTranslitAndType(translit, 2L, author, false);
    }
}
