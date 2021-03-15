package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.repositories.PostsRepo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@NoArgsConstructor
public class PostsService {
    private PostsRepo postsRepo;

    @Autowired
    public void setPostsRepo(PostsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }

    public HashMap<String, Object> findTopPosts(boolean author){
        HashMap<String, Object> result = new LinkedHashMap<>();
        List<Posts> list = postsRepo.findTopPosts(PageRequest.of(0, 6));
        for (int i=0; i<list.size(); i++){
            result.put(String.valueOf(i), list.get(i));
        }
        return result;
    }

    public List<Posts> findAllPosts(){
       return postsRepo.findAll();
    }
}
