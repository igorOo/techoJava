package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "social_type")
public class SocialType {

    @Id
    @GeneratedValue
    @PrimaryKeyJoinColumn
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_create")
    private Date date_create;
}
