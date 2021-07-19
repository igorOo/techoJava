package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "`user`", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User implements Serializable {

    @Id
    @GeneratedValue
    @PrimaryKeyJoinColumn
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonView(JsonViewer.Internal.class)
    private String name;

    @Column(name = "email", nullable = false)
    @JsonView(JsonViewer.Internal.class)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "avatar")
    @JsonView(JsonViewer.Public.class)
    private String avatar;

    @Column(name = "rating")
    @JsonView(JsonViewer.Public.class)
    private int rating;

    @Column(name = "auth_key")
    @JsonIgnore
    private String auth_key;

    @Column(name = "access_token")
    @JsonIgnore
    private String access_token;

    @Column(name = "birth")
    @JsonView(JsonViewer.Internal.class)
    private Date birth;

    @Column(name = "gender")
    @JsonView(JsonViewer.Public.class)
    private GenderEnum gender;

    @Column(name = "first_name")
    @JsonView(JsonViewer.Public.class)
    private String firstName;

    @Column(name = "last_name")
    @JsonView(JsonViewer.Public.class)
    private String lastName;

    @OneToOne
    @JoinColumn(name = "social")
    @JsonView(JsonViewer.Internal.class)
    private SocialType social;

    @Column(name = "id_social")
    @JsonView(JsonViewer.Internal.class)
    private String id_social;

    @Column(name = "public")
    @JsonView(JsonViewer.Internal.class)
    private int status;

    @Column(name = "date_create")
    @JsonView(JsonViewer.Internal.class)
    private Date date_create;

    @Column(name = "date_edit")
    @JsonView(JsonViewer.Internal.class)
    private Date date_edit;

    @Column(name = "last_visit")
    @JsonView(JsonViewer.Internal.class)
    private Date last_visit;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @JsonIgnore
    private List<Roles> roles;

    private String about;

    public boolean isActive(){
        return status == 1;
    }
}
