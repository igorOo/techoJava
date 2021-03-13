package ru.technoteinfo.site.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.technoteinfo.site.services.SubscribeService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/subscribe")
public class SubscribeController {

    private SubscribeService subscribeService;

    @Autowired
    public void setSubscribeService(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PostMapping(value = "/add")
    @ResponseBody
    public String subscribe(
            @RequestParam(value = "email") String email,
            HttpServletRequest request
    ){
        subscribeService.add(email, request.getRemoteAddr());
        return "ok";
    }


}
