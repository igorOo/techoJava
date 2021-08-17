package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts_type")
public class PostsType {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name = "post_type")
    private Long postType;

    @Column(name = "name")
    private String name;
}
