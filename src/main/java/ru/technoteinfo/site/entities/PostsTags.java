package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts_tags")
public class PostsTags {

    @EmbeddedId
    private PostsTagsPK id;

    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false, insertable = false, updatable = false)
    private Posts post;

    @Column(name = "tags", insertable = false, updatable = false)
    @JsonView(JsonViewer.ExtendedPublic.class)
    private String tag;
}


@Embeddable
class PostsTagsPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="id_post")
    private Long postId;

    @Column(name="tags")
    private String tags;
}