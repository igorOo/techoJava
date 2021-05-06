package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Category;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.PostsType;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.CategoryRepo;
import ru.technoteinfo.site.repositories.PostsRepo;
import ru.technoteinfo.site.repositories.impl.PostsRepoImpl;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class NewsCategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private PostsRepoImpl postsRepo;

    @Autowired
    private CommonController common;

    public List<TopPost> getCategoryList(String translit, int page, int pageSize){
        Sort sort = Sort.by(Sort.Direction.DESC, "sort");
        Category category = categoryRepo.findFirstByTranslitAndTypePost_PostType(translit, 1L, sort);
        List<TopPost> list = postsRepo.findPostsInCategoryAndType(category.getTranslit(), category.getTypePost().getPostType(), false, true, page, pageSize);
        common.formatMeta(list);
        return list;
    }

    public Integer getCountPostsInCategory(String category){
        return postsRepo.getCountPostsInCategory(category);
    }

    public Integer getCountPostsByType(Long type){
        return postsRepo.getCountPostsByType(type);
    }

    public List<TopPost> getPostsByType(Long type, boolean author, boolean preview, int page, int pageSize){
        Sort sort = Sort.by(Sort.Direction.DESC, "sort");
        List<TopPost> list = postsRepo.findPostsbyType(type, author, preview, pageSize, (page-1)*pageSize);
        common.formatMeta(list);
        return list;
    }

}
