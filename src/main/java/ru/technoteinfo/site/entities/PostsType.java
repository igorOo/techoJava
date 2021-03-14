package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "posts_type")
public class PostsType {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "post_type")
    private int postType;

    @Column(name = "name")
    private String name;
}
