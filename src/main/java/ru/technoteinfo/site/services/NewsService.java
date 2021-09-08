package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.HotNewsRepository;
import ru.technoteinfo.site.repositories.impl.PostsRepositoryImpl;

import java.util.List;

@Service
@NoArgsConstructor
@Slf4j
public class NewsService extends DetailService{
    @Autowired
    private PostsRepositoryImpl postsRepo;

    @Autowired
    private CommonController common;

    @Autowired
    private HotNewsRepository hotNewsRepository;

    public List<TopPost> findTopReaderPosts(){
        List<TopPost> list = postsRepo.findTopReaderPosts(false, true, 6, 1L);
        common.formatMeta(list);
        return list;
    }

    public List<TopPost> findRandomImagePosts(){
        List<TopPost> list = postsRepo.findRandomImagePosts(1L, 6);
        common.formatMeta(list);
        return list;
    }


}
