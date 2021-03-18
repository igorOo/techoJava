package ru.technoteinfo.site.repositories.impl;

import lombok.Value;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.technoteinfo.site.entities.Category;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.PostsType;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.PostsRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        return getMainPostsWithId(id, author, limit, 0);
    }

    @Override
    public List<TopPost> findMainPosts(String translit, boolean author, int limit) {
        return getMainPostsTranslit(translit, author, limit, 0);
    }

    @Override
    public List<TopPost> findMainPosts(Long id, boolean author, int limit, int start) {
        return getMainPostsWithId(id, author, limit, start);
    }

    @Override
    public List<TopPost> findMainPosts(String translit, boolean author, int limit, int start) {
        return getMainPostsTranslit(translit, author, limit, start);
    }

    @Override
    public List<TopPost> findPostsbyType(Long type, boolean author, int limit, int start){
        return getPostsByType(type, author, limit, start);
    }

    public Integer getCountPostsByType(Long typePost){
        try {
            final String queryString = "select count(p1) from Posts p1 where p1.type= :type_post";
            Query query = entityManager.createNativeQuery(queryString);
            query.setParameter("type_post", typePost);
            Integer result = Integer.valueOf(String.valueOf(query.getSingleResult()));
            return result;
        } catch (RuntimeException re) {
            return 0;
        }
    }

    private List<TopPost> getMainPostsTranslit(String translit, boolean author, int limit, int start) {
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
                .setFirstResult(start)
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

    private List<TopPost> getMainPostsWithId(Long id, boolean author, int limit, int start){
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
                .setFirstResult(start)
                .getResultList();
        List<TopPost> result = new ArrayList<>();
        for (Posts item : resultSql){
            TopPost topPost = new TopPost(
                    item.getId(),
                    item.getName(),
                    item.getTranslit(),
                    item.getMain_image(),
                    item.getType().getPostType(),
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

    private List<TopPost> getPostsByType(Long type, boolean author, int limit, int start){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Posts> query = cb.createQuery(Posts.class);
        Root<Posts> posts = query.from(Posts.class);
        Join<Posts, PostsType> postsTypeJoin = posts.join("type", JoinType.LEFT);
        postsTypeJoin.on(cb.equal(postsTypeJoin.get("postType"), posts.get("type")));
        query.select(posts);
        ParameterExpression<Long> post_type = cb.parameter(Long.class);

        query.where(
                cb.equal(postsTypeJoin.get("postType"), post_type),
                cb.equal(posts.get("status"), 1)
        );
        query.orderBy(cb.desc(posts.get("date_create")));
        if (author){
            Join<Posts, User> authorJoin = posts.join("author", JoinType.LEFT);
            authorJoin.on(cb.equal(authorJoin.get("id"), posts.get("author")));
        }

        List<Posts> resultSql = entityManager.createQuery(query)
                .setParameter(post_type, type)
                .setMaxResults(limit)
                .setFirstResult(start)
                .getResultList();
        List<TopPost> result = new ArrayList<>();
        for (Posts item : resultSql){
            TopPost topPost = new TopPost(
                    item.getId(),
                    item.getName(),
                    item.getTranslit(),
                    item.getMain_image(),
                    item.getType().getPostType(),
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
