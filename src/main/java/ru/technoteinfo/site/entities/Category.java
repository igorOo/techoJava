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
    private BigInteger id;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "translit", nullable = false)
    private String translit;

    @Column(name = "image")
    private String image;

    @Column(name = "public")
    private int status;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private User author;

    @Column(name = "date_create")
    private Date date_create;

    @Column(name = "date_edit")
    private Date date_edit;

    @Column(name = "sort")
    private int sort;

    @OneToOne(mappedBy = "posts_type", fetch = FetchType.EAGER)
    private PostsType type_post;

}