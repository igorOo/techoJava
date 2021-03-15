package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.technoteinfo.site.entities.Posts;

import java.util.List;

public interface PostsRepo extends JpaRepository<Posts, Long> {
    @Query("select p.id, p.name, p.translit, p.main_image, p.type, p.date_create, " +
            "p.category.name as category, p.category.translit as cat_translit, p.category "+
            " from Posts p " +
            " where p.status = 1 " +
            " and p.category.translit = 'gadgets' " +
            " order by p.id desc ")
    List<Posts> findTopPosts(Pageable pageable);
}
