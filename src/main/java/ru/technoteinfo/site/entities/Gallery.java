package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.technoteinfo.site.pojo.CategoryResponse;
import ru.technoteinfo.site.pojo.GalleryResponse;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "images")
public class Gallery {
    @Id
    private Long id;

    private String name;
    private String translit;
    private String resolution;
    private String filename;
    private String alt;

    @OneToOne
    @JoinColumn(name = "category")
    private Category category;
    private String tags;

    @Column(name = "date_create")
    private Date dateCreate;

    @Column(name = "date_edit")
    private Date dateEdit;

    public GalleryResponse toGalleryResponse(){
        return new GalleryResponse(
            this.getId(),
            this.getName().trim(),
            this.getTranslit(),
            this.getResolution().trim(),
            this.getFilename().trim(),
            this.getAlt().trim(),
            new CategoryResponse(
                this.getCategory().getId(),
                this.getCategory().getName(),
                this.getCategory().getTranslit()
            ),
            this.getTags()
        );
    }
}
