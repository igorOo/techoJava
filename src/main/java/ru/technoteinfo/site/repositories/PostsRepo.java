package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;

import java.math.BigInteger;
import java.util.List;

public interface PostsRepo extends JpaRepository<Posts, Long> {
    @Query("select new ru.technoteinfo.site.entities.queriesmodels.TopPost(p.id, p.name, p.translit, p.main_image, p.type.id, p.date_create, " +
            " p.category.name, p.category.translit, p.category.id)"+
            " from Posts p " +
            " where p.status = 1 " +
            " and p.category.id = :id " +
            " order by p.id desc ")
    List<TopPost> findTopPosts(Pageable pageable, @Param("id") Long id);
}

/*
p.id, p.name, p.translit, p.main_image, p.type, p.date_create, " +
        "p.category.name as category, p.category.translit as cat_translit, p.category */
