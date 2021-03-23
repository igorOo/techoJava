package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "serial")
    @JsonView(JsonViewer.Public.class)
    private Long id;

    @OneToOne
    @JoinColumn(name = "parent_id")
    @JsonView(JsonViewer.Public.class)
    private Category parentCategory;

    @Column(name = "name", nullable = false)
    @JsonView(JsonViewer.Public.class)
    private String name;

    @Column(name = "translit", nullable = false)
    @JsonView(JsonViewer.Public.class)
    private String translit;

    @Column(name = "image")
    @JsonView(JsonViewer.Public.class)
    private String image;

    @Column(name = "public")
    @JsonView(JsonViewer.Internal.class)
    private int status;

    @ManyToOne
    @JoinColumn(name = "author")
    @JsonIgnore
    private User author;

    @Column(name = "date_create")
    @JsonIgnore
    private Date date_create;

    @Column(name = "date_edit")
    @JsonIgnore
    private Date date_edit;

    @Column(name = "sort")
    @JsonView(JsonViewer.Public.class)
    private int sort;

    @OneToOne
    @JoinColumn(name = "type_post")
    @JsonView(JsonViewer.Internal.class)
    private PostsType type_post;

}
