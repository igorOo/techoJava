package ru.technoteinfo.site.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    @GetMapping
    @ResponseBody
    public Map<Integer, String> index(){
        Map<Integer, String> list = new HashMap<>();
        for (int i=0; i<=10; i++){
            list.put(i, "Строка: "+i);
        }
        return list;
    }
}
