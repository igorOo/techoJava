package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Bookmarks;
import ru.technoteinfo.site.entities.Posts;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.repositories.BookmarksRepository;
import ru.technoteinfo.site.repositories.UserRepository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarksRepository bookmarksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonController commonController;

    public List<TopPost> getListBookmarks(Pageable pageable, Long userId){
        List<Bookmarks> resultBookmarks = bookmarksRepository.findByUserId(userId, pageable);
        List<TopPost> result = new ArrayList<>();
        for (Bookmarks bookmark: resultBookmarks){
            result.add(bookmark.getPosts().toTopPost());
        }
        commonController.formatMeta(result);
        return result;
    }

    public void add(TechnoUserDetail user, Long postId){
        Bookmarks bookmarks = new Bookmarks();
        bookmarks.setPostId(postId);
        bookmarks.setUserId(user.getId());
        bookmarksRepository.save(bookmarks);
    }

    public void delete(Long postId, Long userId){
        Bookmarks bookmark = bookmarksRepository.findByPostIdAndUserId(postId, userId);
        bookmarksRepository.delete(bookmark);
    }

    public boolean check(Long postId, Long userId){
        Bookmarks bookmark = bookmarksRepository.findByPostIdAndUserId(postId, userId);
        return bookmark != null;
    }

    public Number getCountBookmark(Long userId){
        return bookmarksRepository.countByUserId(userId);
    }
}
