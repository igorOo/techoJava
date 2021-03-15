package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "social_type")
public class SocialType {

    @Id
    @GeneratedValue
    @PrimaryKeyJoinColumn
    private Float id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_create")
    private Date date_create;
}
