package ru.technoteinfo.site.controllers.common;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.pojo.response.GalleryResponse;

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
        if (avatar.matches("^http.*")){
            return avatar;
        }else{
            return securityImage+"://"+domainImage+"/"+avatar;
        }
    }

    public String getNoteUrl(String slug) { return urlSecurity+"://"+urlDomain+"/note/"+slug;}

    public String getNewUrl(String slug) { return urlSecurity+"://"+urlDomain+"/new/"+slug;}

    public List<GalleryResponse> formatGalerryList(List<GalleryResponse> list){
        for (GalleryResponse item: list){
            String image = item.getFilename();
            if (image != null){
                item.setFilename(securityImage+"://"+domainImage+"/images/gallery/thumb/"+item.getCategory().getId()+"/"+image);
            }
            item.setUrl(urlSecurity+"://"+urlDomain+"/gallery/"+item.getTranslit());
        }
        return list;
    }

    public GalleryResponse formatGalleryItem(GalleryResponse item, boolean detail){
        String urlPart = "thumb";
        if (detail == true){
            urlPart = "view";
        }
        String image = item.getFilename();
        if (image != null){
            item.setFilename(securityImage+"://"+domainImage+"/images/gallery/"+urlPart+"/"+item.getCategory().getId()+"/"+image);
        }
        item.setUrl(urlSecurity+"://"+urlDomain+"/gallery/"+item.getTranslit());
        return item;
    }

    public static String randomString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
