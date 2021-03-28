package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.StatisticPostRead;
import ru.technoteinfo.site.services.StatisticService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/metrika")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @RequestMapping(value = "/add-time-read-post", method = RequestMethod.POST)
    public void SetStatisticReadPost(
            @RequestBody StatisticPostRead body,
            HttpServletRequest request
    ){
       statisticService.savePostReadTime(body, request.getRemoteAddr());
    }
}
