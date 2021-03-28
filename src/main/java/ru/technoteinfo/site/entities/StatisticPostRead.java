package ru.technoteinfo.site.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "statistic_post_read")
public class StatisticPostRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Posts post;

    @Column(name = "time_read")
    private Float timeRead;

    @ManyToOne
    @Nullable
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ip_address")
    @Nullable
    private String ipAddress;

    @Column(name = "date_create")
    private String dateCreate;
}
