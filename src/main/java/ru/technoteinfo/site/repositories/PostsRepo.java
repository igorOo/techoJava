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
    List<TopPost> findMainPosts(Long id, boolean author, boolean preview, int limit);
    List<TopPost> findMainPosts(String translit, boolean author, boolean preview, int limit);
    List<TopPost> findMainPosts(Long id, boolean author, boolean preview, int limit, int start);
    List<TopPost> findMainPosts(String translit, boolean author, boolean preview, int limit, int start);
    List<TopPost> findPostsbyType(Long type, boolean author, boolean preview, int limit, int start);

    Posts findPostByTranslit(String translit, boolean author, boolean meta);
}
