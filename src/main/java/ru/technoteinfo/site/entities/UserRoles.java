package ru.technoteinfo.site.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_id", updatable = false, insertable = false)
    @OneToMany
    private List<Roles> roleIdList;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_id", updatable = false, insertable = false)
    @OneToMany
    private List<User> userIdList;
}
