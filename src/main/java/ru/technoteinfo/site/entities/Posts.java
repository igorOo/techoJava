package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    @JsonView(JsonViewer.Public.class)
    private Long id;

    @OneToOne
    @JoinColumn(name = "category_id")
    @JsonView(JsonViewer.Public.class)
    private Category category;

    @JsonView(JsonViewer.Public.class)
    private String name;

    @Column(unique = true)
    @JsonView(JsonViewer.Public.class)
    private String translit;

    @JsonView(JsonViewer.Public.class)
    private String preview;

    @JsonView(JsonViewer.ExtendedPublic.class)
    private String text;

    @JsonView(JsonViewer.Public.class)
    private String descriptions;

    @JsonView(JsonViewer.Public.class)
    private String keywords;

    @JsonView(JsonViewer.Public.class)
    private String main_image;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    @JsonView(JsonViewer.Public.class)
    private User author;

    @JsonView(JsonViewer.Public.class)
    private int rating;

    @JsonView(JsonViewer.Public.class)
    private String date_create;

    @JsonView(JsonViewer.Internal.class)
    private String date_edit;

    @JsonView(JsonViewer.Internal.class)
    private String ip_address;

    @Column(name = "public")
    @JsonView(JsonViewer.Internal.class)
    private int status;

    @OneToOne
    @JoinColumn(name = "type")
    @JsonView(JsonViewer.Internal.class)
    private PostsType type;

    @JsonIgnore
    private String post_vector;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @Column(insertable = false, updatable = false)
    @JsonView(JsonViewer.ExtendedPublic.class)
    transient private List<PostsTags> tags;

}
