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
import ru.technoteinfo.site.repositories.impl.PostsRepoImpl;

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
            List<TopPost> posts = postsRepo.findMainPosts(
                    Long.valueOf(String.valueOf(obj[0])),
                    author,
                    5
            );
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

}
