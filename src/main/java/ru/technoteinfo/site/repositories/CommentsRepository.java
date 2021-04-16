package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.technoteinfo.site.entities.Comments;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByPostIdIgnoreCase(String postId, Pageable pageable);
    Integer countByPostId(String postId);
}
