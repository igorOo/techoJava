package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.CategoryRepo;
import ru.technoteinfo.site.repositories.PostsRepo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Service
@NoArgsConstructor
public class PostsService {
    private PostsRepo postsRepo;
    private CategoryRepo categoryRepo;

    @Autowired
    private CommonController common;

    @Autowired
    public void setPostsRepo(PostsRepo postsRepo) {
        this.postsRepo = postsRepo;
    }

    @Autowired
    public void setCategoryRepo(CategoryRepo categoryRepo) { this.categoryRepo = categoryRepo; }

    public HashMap<String, List<TopPost>> findTopPosts(boolean author, HttpServletRequest request){
        HashMap<String, List<TopPost>> result = new LinkedHashMap<>();
        List<Object> categories = categoryRepo.findTopViewCategories();

        for (Object category: categories){
            Object[] obj = (Object[]) category;
            List<TopPost> posts = postsRepo.findTopPosts(PageRequest.of(0, 5), Long.valueOf(String.valueOf(obj[0])));
            common.formatMeta(posts, request);
            result.put(String.valueOf(obj[0]), posts);
        }

        return result;
    }

    public List<Posts> findAllPosts(){
       return postsRepo.findAll();
    }


}
