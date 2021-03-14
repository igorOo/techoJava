package ru.technoteinfo.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.technoteinfo.site.entities.Posts;

import java.util.List;

public interface PostsRepo extends JpaRepository<Posts, Long> {
    @Query("select p.id, p.name, p.translit, p.main_image, p.type, p.date_create, " +
            "c1.name as category, c1.translit as cat_translit, p.category_id "+
            " from Posts p " +
            " left Join Category c1 " +
            " where p.status = 1 " +
            " and c1.translit = 'gadgets' " +
            " order by p.id desc ")
    List<Posts> findTopPosts();
}
