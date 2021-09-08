package ru.technoteinfo.site.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.technoteinfo.site.pojo.response.CategoryResponse;

@Data
@AllArgsConstructor
public class GalleryResponse {
    private Long id;
    private String name;
    private String translit;
    private String resolution;
    private String filename;
    private String alt;
    private String url;
    private CategoryResponse category;
    private String tags;

    public GalleryResponse(Long id,String name,String translit,String resolution,String filename,String alt,CategoryResponse category,String tags){
        this.id = id;
        this.name = name;
        this.translit = translit;
        this.resolution = resolution;
        this.filename = filename;
        this.alt = alt;
        this.category = category;
        this.tags = tags;
    }
}
