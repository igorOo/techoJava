package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.PostsTags;
import ru.technoteinfo.site.repositories.impl.PostsRepoImpl;

import java.util.List;

@Service
@NoArgsConstructor
public class NewsService {
    @Autowired
    private PostsRepoImpl postsRepo;

    @Autowired
    private CommonController common;

    public Posts findNewByTranslit(String translit, boolean author, boolean meta){
        Posts post = postsRepo.findPostByTranslit(translit, author, meta);
        common.formatMetaPost(post);
        return post;
    }
}
