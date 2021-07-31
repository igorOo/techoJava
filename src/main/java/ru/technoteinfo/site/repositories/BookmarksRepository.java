package ru.technoteinfo.site.repositories;

import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.technoteinfo.site.entities.Bookmarks;

import java.util.List;

public interface BookmarksRepository extends JpaRepository<Bookmarks, Long> {
    public List<Bookmarks> findByUserId(Long userId, Pageable pageable);
    public Bookmarks findByPostIdAndUserId(Long postId, Long userId);
    public Integer countByUserId(Long userId);
}
