package ru.technoteinfo.site.services;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.impl.PostsRepositoryImpl;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private PostsRepositoryImpl postsRepoImpl;

    @Autowired
    private CommonController commonController;

    public List<TopPost> search(@NotNull String search, int perPage, int page){
        return commonController.formatMeta(postsRepoImpl.searchPosts(search, perPage, (page - 1) * perPage));
    }

}
