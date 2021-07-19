package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "posts_bookmarks")
public class Bookmarks {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "serial")
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Posts posts;
}
