package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.technoteinfo.site.entities.Category;

import java.lang.annotation.Native;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    @Query(value = "select p1.category_id, sum(pv.views) as views from posts as p1 "+
                    " left join post_views as pv on pv.post_id = p1.id "+
                    " where p1.public = 1 "+
                    " group by category_id "+
                    " order by views desc"+
                    " limit 4; ",
            nativeQuery = true)
    List<Object> findTopViewCategories();

}