package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;

    @Column(unique = true)
    private String translit;

    private String preview;

    private String text;

    private String descriptions;

    private String keywords;

    private String main_image;

    @OneToOne
    @JoinColumn(name = "author")
    private User author;

    private int rating;

    private Date date_create;

    private Date date_edit;

    private String ip_address;

    @Column(name = "public")
    private int status;

    @OneToOne
    @JoinColumn(name = "type")
    private PostsType type;

    private String post_vector;

}
