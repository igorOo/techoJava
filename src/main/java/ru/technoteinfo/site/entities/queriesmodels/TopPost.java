package ru.technoteinfo.site.entities.queriesmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class TopPost {
    private Long id;
    private String name;
    private String translit;
    private String main_image;
    private Long type;
    private String date_create;
    private String category;
    private String cat_translit;
    private Long category_id;
    private String url;
    private String first_name;
    private String last_name;

    public TopPost(Long id, String name, String translit, String main_image, Long type,
            Date date_create, String category, String cat_translit, Long category_id
    ){
        this.id = id;
        this.name = name;
        this.translit = translit;
        this.main_image = main_image;
        this.type = type;
        this.date_create = date_create.toString();
        this.category = category;
        this.cat_translit = cat_translit;
        this.category_id = category_id;
    }

    public TopPost(Long id, String name, String translit, String main_image, Long type,
                   Date date_create, String category, String cat_translit, Long category_id,
                   String url
    ){
        this.id = id;
        this.name = name;
        this.translit = translit;
        this.main_image = main_image;
        this.type = type;
        this.date_create = date_create.toString();
        this.category = category;
        this.cat_translit = cat_translit;
        this.category_id = category_id;
        this.url = url;
    }
}
