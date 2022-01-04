package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.pojo.response.NextPrevResponse;
import ru.technoteinfo.site.repositories.impl.PostsRepositoryImpl;

import java.math.BigInteger;
import java.util.List;

@Service
@NoArgsConstructor
@Slf4j
public class DetailService {

    @Autowired
    private PostsRepositoryImpl postsRepo;

    @Autowired
    private CommonController common;

    public Posts findPostByTranslitAndType(String translit, Long postType, boolean author, boolean meta){
        Posts post = postsRepo.findPostByTranslitAndType(translit, postType, author, meta);
        Double postTimeRead = postsRepo.getTimeReadPost(post.getId());
        BigInteger postcountRead = postsRepo.getCountReadPost(post.getId());

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
        post.setCountRead(postcountRead);
        common.formatMetaPost(post);
        return post;
    }

    public List<TopPost> findSimilarPosts(String category, Long typePost){
        List<TopPost> list = postsRepo.findSimilarPosts(category, false, false, 3, typePost);
        common.formatMeta(list);
        return list;
    }

    public List<NextPrevResponse> getPrevAndNextPosts(Long post_id, Long typePost){
        return postsRepo.getNextAndPrevPosts(post_id, typePost);
    }

}
