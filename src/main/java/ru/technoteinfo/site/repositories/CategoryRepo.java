package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.technoteinfo.site.entities.Category;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    @Query(value = "select p1.category_id, sum(pv.views) as views from posts as p1 "+
                    " left join post_views as pv on pv.post_id = p1.id "+
                    " where p1.public = 1 "+
                    " group by category_id "+
                    " order by views desc"+
                    " limit 4; ",
            nativeQuery = true)
    List<Object> findTopViewCategories();

    Category findFirstByTranslitAndTypePost_PostType(String translit, Long postsType, Sort sort);

    @Query(value = "select cat1 from Category as cat1" +
                    " where cat1.typePost.postType = :type and cat1.status = 1 order by cat1.sort asc")
    List<Category> findCategoryByTypePost(@Param("type") Long type);

    List<Category> findByParentCategoryIsNull();
}
