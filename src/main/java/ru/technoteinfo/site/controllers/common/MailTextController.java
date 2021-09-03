package ru.technoteinfo.site.controllers.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.technoteinfo.site.entities.User;

public class MailTextController {

    public static String confirmText(User user, String url){
        String str = "Привет, "+user.getEmail()+". <br>\n" +
                "Для активации перейдите по ссылке<br> <a href=\""+url+"/user/activate-account/"+user.getAuthKey()+"\">\n" +
                "    "+url+"/user/activate-account/"+user.getAuthKey()+"\n" +
                "</a>";

        return str;
    }

    public static String resetPasswordText(User user, String url){
        return "Привет, "+ user.getEmail()+". <br>\n" +
                "Для восстановления пароля перейдите по ссылке<br> <a href=\""+url+"/confirm-reset-password/"+user.getAuthKey()+"\">\n" +
                "    "+url+"/confirm-reset-password/"+user.getAuthKey()+"\n" +
                "</a>";
    }
}
