package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;

import javax.persistence.*;

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
    private String from;

    @JsonProperty("avatar")
    @JsonView(JsonViewer.Public.class)
    @Transient
    private String avatar;

    @JsonProperty("text")
    @JsonView(JsonViewer.Public.class)
    private String text;

    private boolean deleted;

    @OneToOne
    @JoinColumn(name = "created_by", updatable = false, insertable = false)
    private User createdBy;

    @OneToOne
    @JoinColumn(name = "updated_by", updatable = false, insertable = false)
    private User updatedBy;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    @JsonView(JsonViewer.Public.class)
    private String createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    @JsonView(JsonViewer.Public.class)
    private String updatedAt;
}
