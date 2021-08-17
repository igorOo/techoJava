package ru.technoteinfo.site.controllers.common;

import ru.technoteinfo.site.entities.User;

public class MailTextController {

    public static String confirmText(User user, String url){
        String str = "Привет "+user.getEmail()+". <br>\n" +
                "Для активации перейдите по ссылке<br> <a href=\""+url+"/user/activate-account/"+user.getAuth_key()+"\">\n" +
                "    "+url+"/user/activate-account/"+user.getAuth_key()+"\n" +
                "</a>";

        return str;
    }
}
