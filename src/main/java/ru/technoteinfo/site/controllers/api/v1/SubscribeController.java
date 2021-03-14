package ru.technoteinfo.site.controllers.api.v1;

import lombok.Data;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.services.SubscribeService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/subscribe")
public class SubscribeController {

    private SubscribeService subscribeService;

    @Autowired
    public void setSubscribeService(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String subscribe(
            @RequestParam(value = "email") String email,
            HttpServletRequest request
    ){
        try {
            subscribeService.add(email, request.getRemoteAddr());
        }catch (SQLException e){
            return e.getLocalizedMessage();
        }
        return "ok";
    }

    @ExceptionHandler(SQLException.class)
    public HashMap<String, Object>  handleException(SQLException e) {
        String message = e.getSQLState().equals("23505") ? "Такая почта уже была сохранена" : "Ошибка БД";
        HashMap<String, Object> response = new LinkedHashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }


}
