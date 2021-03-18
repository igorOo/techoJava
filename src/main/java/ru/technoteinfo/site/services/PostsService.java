package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.CategoryRepo;
import ru.technoteinfo.site.repositories.impl.PostsRepoImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@NoArgsConstructor
public class PostsService {
    @Autowired
    private PostsRepoImpl postsRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CommonController common;

    public HashMap<String, List<TopPost>> findTopPosts(boolean author){
        HashMap<String, List<TopPost>> result = new LinkedHashMap<>();
        List<Object> categories = categoryRepo.findTopViewCategories();

        for (Object category: categories){
            Object[] obj = (Object[]) category;
            List<TopPost> posts = postsRepo.findMainPosts(Long.valueOf(String.valueOf(obj[0])), author,5);
            common.formatMeta(posts);
            result.put(String.valueOf(obj[0]), posts);
        }

        return result;
    }

    public List<TopPost> findGadgetPosts(boolean author, String translit){
        List<TopPost> result = postsRepo.findMainPosts(translit, author, 6);
        common.formatMeta(result);
        return result;
    }

    public HashMap<String, List<TopPost>> findInterAndProgsPosts(boolean author, String[] translits){
        HashMap<String, List<TopPost>> result = new LinkedHashMap<>();
        for (String translit : translits){
            List<TopPost> list = postsRepo.findMainPosts(translit, author, 6);
            common.formatMeta(list);
            result.put(list.get(0).getCategory_id().toString(), list);
        }
        return  result;
    }

    public List<TopPost> findHardwarePosts(boolean author, String translit){
        List<TopPost> result = postsRepo.findMainPosts(translit, author, 4);
        common.formatMeta(result);
        return result;
    }

    public HashMap<String, Object> findArticles(int page, boolean author){
        Integer limit = 6;
        HashMap<String, Object> result = new HashMap<>();
        List<TopPost> articles = postsRepo.findPostsbyType(2L, author, limit, 6*(page-1));
        common.formatMeta(articles);
        HashMap<String, Integer> pagination = new HashMap<>();
        Integer countRows = postsRepo.getCountPostsByType(2L);
        pagination.put("currentPage", page);
        pagination.put("totalRows", countRows);
        pagination.put("totalPages", (int) Math.ceil(countRows.doubleValue()/limit.doubleValue()));
        result.put("data", articles);
        result.put("pagination", pagination);
        return result;
    }

}
