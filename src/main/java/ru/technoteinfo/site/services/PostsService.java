package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.CategoryRepo;
import ru.technoteinfo.site.repositories.PostsRepo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@NoArgsConstructor
public class PostsService {
    private PostsRepo postsRepo;
    private CategoryRepo categoryRepo;

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
            this.formatMeta(posts, request);
            result.put(String.valueOf(obj[0]), posts);
        }

        return result;
    }

    public List<Posts> findAllPosts(){
       return postsRepo.findAll();
    }

    private List<TopPost> formatMeta(List<TopPost> list, HttpServletRequest request){
        for (TopPost item : list){
            String image = item.getMain_image();
            if (image != null){
                item.setMain_image("https://"+request.getServerName()+"/images/post/"+item.getId()+"/"+image);
            }
            System.out.println(item.getDate_create());
            LocalDate localDate = LocalDate.parse(item.getDate_create(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.A"));
            DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("dd MMM uuuu");
            item.setDate_create(formmat1.format(localDate));
        }
        return list;
    }
}
