package ru.technoteinfo.site.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonValueInstantiator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "`user`", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PrimaryKeyJoinColumn
    private Long id;

    @Column(name = "name")
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
    private String authKey;

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
    private String idSocial;

    @Column(name = "public")
    @JsonView(JsonViewer.Internal.class)
    private int status;

    @Column(name = "date_create")
    @JsonView(JsonViewer.Internal.class)
    private Date dateCreate;

    @Column(name = "date_edit")
    @JsonView(JsonViewer.Internal.class)
    private Date dateEdit;

    @Column(name = "last_visit")
    @JsonView(JsonViewer.Internal.class)
    private Date lastVisit;

    @OneToMany()
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", insertable = false, updatable = false)
    )
    @JsonIgnore
    private List<Roles> roles = new ArrayList<>();

    private String about;

    public boolean isActive(){
        return status == 1;
    }
}
