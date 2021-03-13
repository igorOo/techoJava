package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "subscribe_emails")
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "email")
    private String email;

    @Column(name = "date_create")
    private Date date;

    @Column(name = "ip_address")
    private String ip_address;
}
