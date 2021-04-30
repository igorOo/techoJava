package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.impl.PostsRepoImpl;

import java.util.List;

@Service
@NoArgsConstructor
@Slf4j
public class DetailService {

    @Autowired
    private PostsRepoImpl postsRepo;

    @Autowired
    private CommonController common;

    public Posts findPostByTranslitAndType(String translit, Long postType, boolean author, boolean meta){
        Posts post = postsRepo.findPostByTranslitAndType(translit, postType, author, meta);
        Double postTimeRead = postsRepo.getTimeReadPost(post.getId());

        String attribute = " сек.";
        if (String.valueOf(postTimeRead).length() > 2 && postTimeRead > 60){
            postTimeRead = Math.ceil(postTimeRead/60);
            attribute = " мин.";
            if (String.valueOf(postTimeRead).length() > 2 && postTimeRead>60){
                postTimeRead = Math.ceil(postTimeRead/60);
                attribute = " ч.";
            }
        }

        post.setReadTime(String.valueOf(postTimeRead) + attribute);
        common.formatMetaPost(post);
        return post;
    }

    public List<TopPost> findSimilarPosts(String category){
        List<TopPost> list = postsRepo.findSimilarPosts(category, false, false, 3, 1L);
        common.formatMeta(list);
        return list;
    }


}
