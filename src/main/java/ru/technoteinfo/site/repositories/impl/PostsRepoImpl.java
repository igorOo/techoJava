package ru.technoteinfo.site.repositories.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.technoteinfo.site.entities.Category;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.PostsRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostsRepoImpl implements PostsRepo {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<TopPost> findMainPosts(Long id, boolean author, int limit) {
        return getMainPostsWithId(id, author, limit);
    }

    @Override
    public List<TopPost> findMainPosts(String translit, boolean author, int limit) {
        return getMainPostsTranslit(translit, author, limit);
    }

    private List<TopPost> getMainPostsTranslit(String translit, boolean author, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Posts> query = cb.createQuery(Posts.class);
        Root<Posts> posts = query.from(Posts.class);
        Join<Posts, Category> categoryJoin = posts.join("category", JoinType.LEFT);
        categoryJoin.on(cb.equal(categoryJoin.get("id"), posts.get("category")));
        query.select(posts);
        ParameterExpression<String> translitCat = cb.parameter(String.class);

        query.where(
                cb.equal(categoryJoin.get("translit"), translitCat),
                cb.equal(posts.get("status"), 1)
        );
        query.orderBy(cb.desc(posts.get("id")));
        if (author){
            Join<Posts, User> authorJoin = posts.join("author", JoinType.LEFT);
            authorJoin.on(cb.equal(authorJoin.get("id"), posts.get("author")));
        }

        List<Posts> resultSql = entityManager.createQuery(query)
                .setParameter(translitCat, translit)
                .setMaxResults(limit)
                .getResultList();
        List<TopPost> result = new ArrayList<>();
        for (Posts item : resultSql){
            TopPost topPost = new TopPost(
                    item.getId(),
                    item.getName(),
                    item.getTranslit(),
                    item.getMain_image(),
                    item.getType().getId(),
                    item.getDate_create(),
                    item.getCategory().getName(),
                    item.getCategory().getTranslit(),
                    item.getCategory().getId()
            );
            if (author){
                topPost.setFirst_name(item.getAuthor().getFirstName());
                topPost.setLast_name(item.getAuthor().getLastName());
            }
            result.add(topPost);
        }

        return result;
    }

    private List<TopPost> getMainPostsWithId(Long id, boolean author, int limit){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Posts> query = cb.createQuery(Posts.class);
        Root<Posts> posts = query.from(Posts.class);
        Join<Posts, Category> categoryJoin = posts.join("category", JoinType.LEFT);
        categoryJoin.on(cb.equal(categoryJoin.get("id"), posts.get("category")));
        query.select(posts);
        ParameterExpression<Long> category_id = cb.parameter(Long.class);

        query.where(
                cb.equal(categoryJoin.get("id"), category_id),
                cb.equal(posts.get("status"), 1)
        );
        query.orderBy(cb.desc(posts.get("id")));
        if (author){
            Join<Posts, User> authorJoin = posts.join("author", JoinType.LEFT);
            authorJoin.on(cb.equal(authorJoin.get("id"), posts.get("author")));
        }

        List<Posts> resultSql = entityManager.createQuery(query)
                .setParameter(category_id, id)
                .setMaxResults(limit)
                .getResultList();
        List<TopPost> result = new ArrayList<>();
        for (Posts item : resultSql){
            TopPost topPost = new TopPost(
                    item.getId(),
                    item.getName(),
                    item.getTranslit(),
                    item.getMain_image(),
                    item.getType().getId(),
                    item.getDate_create(),
                    item.getCategory().getName(),
                    item.getCategory().getTranslit(),
                    item.getCategory().getId()
            );
            if (author){
                topPost.setFirst_name(item.getAuthor().getFirstName());
                topPost.setLast_name(item.getAuthor().getLastName());
            }
            result.add(topPost);
        }

        return result;
    }
}
