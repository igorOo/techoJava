package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.technoteinfo.site.entities.Comments;

import java.util.List;

public interface CommentsRepo extends CrudRepository<Comments, Long> {
    List<Comments> findCommentsByPostIdIgnoreCase(String postId, Pageable pageable);
}
