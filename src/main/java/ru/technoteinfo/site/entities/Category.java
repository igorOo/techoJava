package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.pojo.response.CategoryResponse;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "serial")
    @JsonView(JsonViewer.Public.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonView(JsonViewer.Public.class)
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childrens = new ArrayList<>();

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

    @OneToOne
    @JoinColumn(name = "author")
    @JsonIgnore
    private User author;

    @Column(name = "date_create")
    @JsonIgnore
    private Date dateCreate;

    @Column(name = "date_edit")
    @JsonIgnore
    private Date dateEdit;

    @Column(name = "sort")
    @JsonView(JsonViewer.Public.class)
    private int sort;

    @OneToOne
    @JoinColumn(name = "type_post")
    @JsonView(JsonViewer.Internal.class)
    private PostsType typePost;

    @JsonView(JsonViewer.Public.class)
    private transient String url;

    public CategoryResponse toCategoryResponse(){
        return new CategoryResponse(
                this.id,
                this.name,
                this.translit,
                this.childrens.stream().map(Category::toCategoryResponse).collect(Collectors.toList())
        );
    }
}
