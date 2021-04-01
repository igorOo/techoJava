package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comment")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "entity")
    private Long postId;

    private String from;

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
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;
}
