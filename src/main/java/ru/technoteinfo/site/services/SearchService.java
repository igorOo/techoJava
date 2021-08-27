package ru.technoteinfo.site.services;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.PostsRepo;
import ru.technoteinfo.site.repositories.impl.PostsRepoImpl;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private PostsRepoImpl postsRepoImpl;

    @Autowired
    private CommonController commonController;

    public List<TopPost> search(@NotNull String search){
        List<TopPost> result = new ArrayList<>();
        result = commonController.formatMeta(postsRepoImpl.searchPosts(search));
        return result;
    }

}
