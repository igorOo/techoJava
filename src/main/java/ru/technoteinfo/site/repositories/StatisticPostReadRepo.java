package ru.technoteinfo.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.technoteinfo.site.entities.StatisticPostRead;

public interface StatisticPostReadRepo extends JpaRepository<StatisticPostRead, Long> {
}
