package ru.technoteinfo.site.entities;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.technoteinfo.site.entities.Enums.GenderEnum;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TechnoUserDetail implements UserDetails {
    private Long id;
    private String email;
    private String userName;

    @JsonIgnore
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;

    private String FirstName;
    private String LastName;
    private String avatar;
    private Date birthDate;
    private GenderEnum gender;
    private Date lastVisit;
    private Date dateCreate;

    public TechnoUserDetail(User user) {
        this.id = user.getId();
        this.userName = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.authorities = user.getRoles().stream()
                .map((Roles role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        this.FirstName = user.getFirstName();
        this.LastName = user.getLastName();
        this.avatar = user.getAvatar();
        this.birthDate = user.getBirth();
        this.gender = user.getGender();
        this.lastVisit = user.getLastVisit();
        this.dateCreate = user.getDateCreate();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
