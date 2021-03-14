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
    private Long id;

    @OneToOne
    private Category category_id;

    private String name;

    @Column(unique = true)
    private String translit;

    private String preview;

    private String text;

    private String descriptions;

    private String keywords;

    private String main_image;

    @OneToOne
    private User author;

    private int rating;

    private Date date_create;

    private Date date_edit;

    private String ip_address;

    @Column(name = "public")
    private int status;

    @OneToOne
    private PostsType type;

    private String post_vector;

}
