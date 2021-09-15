package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.StatisticPostRead;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.exceptions.CustomForbiddenException;
import ru.technoteinfo.site.services.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/metrika")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @RequestMapping(value = "/add-time-read-post", method = RequestMethod.POST)
    public void setStatisticReadPost(
            @RequestBody StatisticPostRead body,
            HttpServletRequest request
    ){
       statisticService.savePostReadTime(body, request.getRemoteAddr());
    }

    @RequestMapping(value = "/view-post/{postId}", method = RequestMethod.GET)
    public void addViewPost(
            @PathVariable Long postId,
            HttpServletRequest request
    ){
        statisticService.addViewPost(postId, request.getRemoteAddr());
    }

    @RequestMapping(value = "/get-last-view", method = RequestMethod.GET)
    public ResponseEntity<?> getLastPostView(){
        Map<String, Object> result = new HashMap<>();
        try {
            List<TopPost> list = statisticService.getLastViewPost();
            result.put("status", "success");
            result.put("data", list);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (CustomForbiddenException error){
            result.put("status", "error");
            result.put("message", error.getMessage());
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }

    }
}
