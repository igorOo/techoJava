package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.HotNews;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.HotNewsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
@Slf4j
public class HotNewsService {
    @Autowired
    private HotNewsRepository hotNewsRepository;

    @Autowired
    private CommonController common;

    public ResponseEntity<?> getHotNews(){
        List<HotNews> list = hotNewsRepository.findByOrderByCreatedAtDesc();
        List<TopPost> result = new ArrayList<TopPost>();
        for (HotNews post: list) {
            result.add(post.getPostId().toTopPost());
        }
        common.formatMeta(result);
        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("data", result);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
