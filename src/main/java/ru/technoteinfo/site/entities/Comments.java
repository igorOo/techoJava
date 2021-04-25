package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comment")
@Cacheable(false)
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonView(JsonViewer.Public.class)
    private Long id;

    @Column(name = "entity")
    private String postId;

    @JsonProperty("author")
    @JsonView(JsonViewer.Public.class)
    @Column(name = "`from`")
    private String from;

    @JsonProperty("avatar")
    @JsonView(JsonViewer.Public.class)
    @Transient
    private String avatar;

    @JsonProperty("text")
    @JsonView(JsonViewer.Public.class)
    private String text;

    private Integer deleted;

    @OneToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @OneToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private User updatedBy;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    @JsonView(JsonViewer.Public.class)
    @Type(type = "timestamp")
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    @JsonView(JsonViewer.Public.class)
    @Type(type = "timestamp")
    private Date updatedAt;

    @JoinColumn(name = "reply_to")
    @OneToOne
    @JsonView(JsonViewer.Public.class)
    @JsonProperty("reply")
    private Comments replyComment;

    @Transient
    @JsonView(JsonViewer.Public.class)
    @JsonProperty("author_id")
    private Long user_id;
}
