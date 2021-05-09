package ru.technoteinfo.site.controllers.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.pojo.GalleryResponse;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public List<TopPost> formatMeta(List<TopPost> list){
        for (TopPost item : list){
            String image = item.getMain_image();
            if (image != null){
                item.setMain_image(securityImage+"://"+domainImage+"/images/post/"+item.getId()+"/"+image);
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public Posts formatMetaPost(Posts post){
            String image = post.getMain_image();
            if (image != null){
                post.setMain_image(securityImage+"://"+domainImage+"/images/post/"+post.getId()+"/"+image);
            }
            if (post.getCategory() != null){
                StringBuilder url = new StringBuilder();
                url.append(urlSecurity+"://"+urlDomain);
                switch (post.getCategory().getTypePost().getPostType().intValue()){
                    case 1:
                        url.append("/news/category");
                        break;
                    case 2:
                        url.append("/notes/category");
                        break;
                    case 3:
                        url.append("/gallery/category");
                        break;
                }
                url.append("/"+post.getCategory().getTranslit());
                post.getCategory().setUrl(url.toString());
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date =  df.parse(post.getDate_create());
                SimpleDateFormat formmat1 = new SimpleDateFormat("dd MMMM yyyy");
                post.setDate_create(formmat1.format(date));
            }catch (ParseException e){
                System.out.println(e);
            }
        final String regex = "src ?= ?\\\"(\\/images\\/[\\w\\/\\.\\-\\_]+)";
        final String string = post.getText();
        final String subst = "src=\""+securityImage+"://"+domainImage+"$1";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        // The substituted value will be contained in the result variable
        final String result = matcher.replaceAll(subst);

        post.setText(result);
        return post;
    }

    public String getAvatarUrl(String avatar){
        return securityImage+"://"+domainImage+"/images/profiles/"+avatar;
    }

    public String getNoteUrl(String slug) { return urlSecurity+"://"+urlDomain+"/note/"+slug;}

    public String getNewUrl(String slug) { return urlSecurity+"://"+urlDomain+"/new/"+slug;}

    public List<GalleryResponse> formatGalerryList(List<GalleryResponse> list){
        for (GalleryResponse item: list){
            String image = item.getFilename();
            if (image != null){
                item.setFilename(securityImage+"://"+domainImage+"/images/gallery/thumb/"+item.getCategory().getId()+"/"+image);
            }
        }
        return list;
    }

    public GalleryResponse formatGalleryItem(GalleryResponse item){
        String image = item.getFilename();
        if (image != null){
            item.setFilename(securityImage+"://"+domainImage+"/images/gallery/thumb/"+item.getCategory().getId()+"/"+image);
        }
        return item;
    }
}
