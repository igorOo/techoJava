package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comment")
@Cacheable(false)
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "entity")
    private String postId;

    @JsonValue
    private String from;

    @JsonValue
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
    @JsonValue
    private String createdAt;

    @Column(name = "updated_at")
    @JsonValue
    private String updatedAt;
}
