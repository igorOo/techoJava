package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.technoteinfo.site.entities.Comments;

import java.util.List;

public interface CommentsRepo extends JpaRepository<Comments, Long> {
    List<Comments> findCommentsByPostId(Long postId, Pageable pageable);
}
