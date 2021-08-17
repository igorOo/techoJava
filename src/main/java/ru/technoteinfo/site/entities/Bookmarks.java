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
@Table(name = "posts_bookmarks")
public class Bookmarks {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "serial")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true, insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Posts posts;

    @Column(name = "post_id", nullable = false)
    private Long postId;
}
