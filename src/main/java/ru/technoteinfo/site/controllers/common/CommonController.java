package ru.technoteinfo.site.controllers.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@NoArgsConstructor
public class CommonController {
    @Value( "${site.domain}" )
    private String urlDomain;

    @Value("${site.security}")
    private String urlSecurity;

    @Value("${site.domainImage}")
    private String domainImage;

    @Value("${site.securityImage}")
    private String securityImage;

    public List<TopPost> formatMeta(List<TopPost> list, HttpServletRequest request){
        for (TopPost item : list){
            String image = item.getMain_image();
            if (image != null){
                item.setMain_image(securityImage+"://"+domainImage+"/images/post/"+item.getId()+"/"+image);
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            try {

                Date date =  df.parse(item.getDate_create());
                SimpleDateFormat formmat1 = new SimpleDateFormat("dd MMMM yyyy");
                item.setDate_create(formmat1.format(date));
            }catch (ParseException e){
            }
            String typeString = "";
            switch (item.getType().intValue()){
                case 1:
                    typeString = "new";
                    break;
                case 2:
                    typeString = "note";
                    break;
                case 3:
                    typeString = "gallery";
                    break;

            }
            item.setUrl(urlSecurity+"://"+urlDomain+"/"+typeString+"/"+item.getTranslit());
        }
        return list;
    }
}
