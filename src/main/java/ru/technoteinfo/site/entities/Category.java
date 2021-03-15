package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "id")

    private Long id;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "translit", nullable = false)
    private String translit;

    @Column(name = "image")
    private String image;

    @Column(name = "public")
    private int status;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "date_create")
    private Date date_create;

    @Column(name = "date_edit")
    private Date date_edit;

    @Column(name = "sort")
    private int sort;

    @OneToOne
    @JoinColumn(name = "type_post")
    private PostsType type_post;

}
