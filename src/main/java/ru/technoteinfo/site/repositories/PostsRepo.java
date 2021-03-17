package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;

import java.math.BigInteger;
import java.util.List;

public interface PostsRepo {
    List<TopPost> findTopPosts(Pageable pageable, @Param("id") Long id);
    List<TopPost> findGadgetsPosts(Pageable pageable);
}
