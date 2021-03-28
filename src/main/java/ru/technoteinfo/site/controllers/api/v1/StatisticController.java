package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/metrika")
public class StatisticController {

    @RequestMapping("/add-time-read-post/{post_id}/{time}")
    public void SetStatisticReadPost(
            @PathVariable("post_id") Long postId,
            @PathVariable("time") Float time,
            HttpServletRequest request
    ){

    }
}
