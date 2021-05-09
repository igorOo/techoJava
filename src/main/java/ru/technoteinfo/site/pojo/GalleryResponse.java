package ru.technoteinfo.site.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.technoteinfo.site.entities.Category;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@AllArgsConstructor
public class GalleryResponse {
    private String name;
    private String translit;
    private String resolution;
    private String filename;
    private String alt;

    private CategoryResponse category;
    private String tags;
}
