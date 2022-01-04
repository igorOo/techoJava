package ru.technoteinfo.site.controllers.api.v1;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.technoteinfo.site.services.HotNewsService;

@RestController
@NoArgsConstructor
@CrossOrigin(value = "*")
public class HotNewsController {

    @Autowired
    private HotNewsService hotNewsService;

    @RequestMapping(path = "/hot-news", method = RequestMethod.GET)
    public ResponseEntity<?> getHotNews(){
        return hotNewsService.getHotNews();
    }
}
