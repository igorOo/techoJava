package ru.technoteinfo.site.controllers.api.v1;

import lombok.Data;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.services.SubscribeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/subscribe")
public class SubscribeController {
    @Autowired
    private SubscribeService subscribeService;

    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public HashMap<String, Object> subscribe(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "fuckingBot") String bot,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        HashMap<String, Object> result = new LinkedHashMap<>();
        try {
            subscribeService.add(email, request.getRemoteAddr());
        }catch (Exception e){
            result.put("status", "error");
            result.put("message", "Не удалось выполнить подписку на рассылку");
            response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
            return result;
        }
        result.put("status", "success");
        return result;
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
