package ru.technoteinfo.site.controllers.api.v1;

import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Bookmarks;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.entities.queriesmodels.JsonViewer;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.services.BookmarkService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bookmarks")
@CrossOrigin(origins = "*")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping("/list/{page}")
    public ResponseEntity<?> getList(
            @PathVariable(value = "page", required = false) Integer page,
            Authentication auth
    ){
        HashMap<String, Object> result = new HashMap<>();
        if (auth == null){
            return returnAuthError(result);
        }
        if (!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            return returnAuthError(result);
        }
        TechnoUserDetail user = (TechnoUserDetail) auth.getPrincipal();
        final int countPerPage = 5;
        if (page > 0){
            page -= 1;
        }
        Pageable pageable = PageRequest.of(page, countPerPage);

        List<TopPost> postsList = this.bookmarkService.getListBookmarks(pageable, user.getId());
        result.put("status", "success");
        result.put("data", postsList);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/toggle/{post_id}/{user_id}")
    public ResponseEntity<?> toggleBookmark(
            @PathVariable(value = "post_id") Long postId,
            @PathVariable(value = "user_id") Long userId,
            Authentication auth
    ){
        HashMap<String, Object> result = new HashMap<>();
        if (auth == null){
            return returnAuthError(result);
        }
        if (!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            return returnAuthError(result);
        }
        TechnoUserDetail user = (TechnoUserDetail) auth.getPrincipal();
        if (!user.getId().equals(userId)){
            return returnAuthError(result);
        }
        try {
            boolean checked = bookmarkService.check(postId, user.getId());
            if (checked){
                bookmarkService.delete(postId, user.getId());
            }else{
                bookmarkService.add(user, postId);
            }
            result.put("count", bookmarkService.getCountBookmark(user.getId()));
            result.put("status", "success");
        }catch (Exception e){
            result.put("status", "error");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/get-count/{user_id}")
    public ResponseEntity<?> isBookmark(
            @PathVariable(value = "user_id") Long userId,
            Authentication auth
    ){
        HashMap<String, Object> result = new HashMap<>();
        if (auth == null){
            return returnAuthError(result);
        }
        if (!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            return returnAuthError(result);
        }
        TechnoUserDetail user = (TechnoUserDetail) auth.getPrincipal();
        if (!user.getId().equals(userId)){
            return returnAuthError(result);
        }
        try {
            result.put("status", "success");
            result.put("count", bookmarkService.getCountBookmark(user.getId()));
        }catch (Exception e){
            result.put("status", "error");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private ResponseEntity<?> returnAuthError(HashMap<String, Object> result){
        result.put("status", "error");
        result.put("data", "Пользователь не найден");
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }
}
