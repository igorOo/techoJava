package ru.technoteinfo.site.repositories.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.technoteinfo.site.entities.Posts;
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
    public List<TopPost> findTopPosts(Pageable pageable, @Param("id") Long id) {


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Posts> query = cb.createQuery(Posts.class);
        Root<Posts> posts = query.from(Posts.class);
        query.select(posts);
        ParameterExpression<Long> category_id = cb.parameter(Long.class);

        query.where(
                cb.equal(posts.get("category"), category_id),
                cb.equal(posts.get("status"), 1)
        );
        query.orderBy(cb.desc(posts.get("id")));

        List<Posts> resultSql = entityManager.createQuery(query).setParameter(category_id, id).getResultList();
        List<TopPost> result = new ArrayList<>();
        for (Posts item : resultSql){
            result.add(new TopPost(
                    item.getId(),
                    item.getName(),
                    item.getTranslit(),
                    item.getMain_image(),
                    item.getType().getId(),
                    item.getDate_create(),
                    item.getCategory().getName(),
                    item.getCategory().getTranslit(),
                    item.getCategory().getId()
            ));
        }

        return result;
    }

    @Override
    public List<TopPost> findGadgetsPosts(Pageable pageable) {
        List<TopPost> result = new ArrayList<>();
        return  result;
    }
}
