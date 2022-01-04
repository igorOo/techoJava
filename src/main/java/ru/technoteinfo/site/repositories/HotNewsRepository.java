package ru.technoteinfo.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.technoteinfo.site.entities.HotNews;

import java.util.List;

public interface HotNewsRepository extends JpaRepository<HotNews, Long> {
    @Query(nativeQuery = true, value = "select * from posts_hour as h1 order by h1.created_at desc limit 3")
    List<HotNews> findByOrderByCreatedAtDesc();
}