package ru.technoteinfo.site.repositories.impl;

import org.springframework.stereotype.Repository;
import ru.technoteinfo.site.entities.*;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.PostsRepo;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Field;

@Repository
public class PostsRepoImpl implements PostsRepo {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<TopPost> findMainPosts(Long id, boolean author, boolean preview, int limit) {
        return getMainPostsWithId(id, author, preview, limit, 0);
    }

    @Override
    public List<TopPost> findMainPosts(String translit, boolean author, boolean preview, int limit) {
        return getMainPostsTranslit(translit, author, preview, limit, 0);
    }

    @Override
    public List<TopPost> findMainPosts(Long id, boolean author, boolean preview, int limit, int start) {
        return getMainPostsWithId(id, author, preview, limit, start);
    }

    @Override
    public List<TopPost> findMainPosts(String translit, boolean author, boolean preview, int limit, int start) {
        return getMainPostsTranslit(translit, author, preview, limit, start);
    }

    @Override
    public List<TopPost> findPostsbyType(Long type, boolean author, boolean preview, int limit, int start){
        return getPostsByType(type, author, preview, limit, start);
    }

    @Override
    public Posts findPostByTranslit(String translit, boolean author, boolean meta){
        Posts post = (Posts) entityManager.createQuery("select p1 from Posts p1 where p1.translit = :translit")
                .setParameter("translit", translit).getSingleResult();
        if (author == false){
            post.setAuthor(null);
        }
        List<PostsTags> tags = entityManager.createQuery("select tg from PostsTags tg where tg.post.id = :post_id")
                .setParameter("post_id", post.getId()).getResultList();
        if (tags != null){
            post.setTags(tags);
        }
        return post;
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

    public List<TopPost> findSimilarPosts(String translit, boolean author, boolean preview, int count, Long type_post){
        if (count == 0){
            count = 3;
        }
        List<Posts> list = entityManager.createQuery("select p1 from Posts p1 " +
                                            "where p1.category.translit=:translit and p1.type.postType = :postType order by random()")
                .setParameter("translit", translit)
                .setParameter("postType", type_post)
                .setMaxResults(count)
                .getResultList();
        List<TopPost> result = new LinkedList<>();
        for (Posts item : list){
            result.add(item.toTopPost(author, preview));
        }
        return result;
    }

    public List<TopPost> findTopReaderPosts(boolean author, boolean preview, int count, Long typePost){
        if (count == 0){
            count = 3;
        }
        List<Object[]> list = entityManager.createQuery("select cast(avg(pr1.timeRead) as int) as time_read, pr1 from StatisticPostRead pr1 " +
                "where pr1.post.type.postType=:postType GROUP BY pr1.post.id, pr1.id")
                .setParameter("postType", typePost)
                .setMaxResults(count)
                .getResultList();
        List<TopPost> result = new LinkedList<>();
        for (Object[] item: list){
            StatisticPostRead postRead = (StatisticPostRead) item[1];
            result.add(postRead.getPost().toTopPost());
        }
        return result;
    }

    private List<TopPost> getMainPostsTranslit(String translit, boolean author, boolean preview, int limit, int start) {
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
            if (preview){
                topPost.setPreview(item.getPreview());
            }
            result.add(topPost);
        }

        return result;
    }

    private List<TopPost> getMainPostsWithId(Long id, boolean author, boolean preview, int limit, int start){
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
            if (preview){
                topPost.setPreview(item.getPreview());
            }
            result.add(topPost);
        }

        return result;
    }

    private List<TopPost> getPostsByType(Long type, boolean author, boolean preview, int limit, int start){
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
            if (preview){
                topPost.setPreview(item.getPreview());
            }
            result.add(topPost);
        }

        return result;
    }


//    public static Map<String, Object> parameters(Object obj) {
//        Map<String, Object> map = new HashMap<>();
//        for (Field field : obj.getClass().getDeclaredFields()) {
//            field.setAccessible(true);
//            try { map.put(field.getName(), field.get(obj)); } catch (Exception e) { }
//        }
//        return map;
//    }

}
