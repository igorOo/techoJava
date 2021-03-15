package ru.technoteinfo.site.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @PrimaryKeyJoinColumn
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "rating")
    private int rating;

    @Column(name = "auth_key")
    private String auth_key;

    @Column(name = "access_token")
    private String access_token;

    @Column(name = "birth")
    private Date birth;

    @Column(name = "gender")
    private GenderEnum gender;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @OneToOne
    @JoinColumn(name = "social")
    private SocialType social;

    @Column(name = "id_social")
    private String id_social;

    @Column(name = "public")
    private int status;

    @Column(name = "date_create")
    private Date date_create;

    @Column(name = "date_edit")
    private Date date_edit;

    @Column(name = "last_visit")
    private Date last_visit;

}
