package ru.technoteinfo.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.technoteinfo.site.entities.UserLog;

import java.time.LocalDate;
import java.util.List;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    int countUserLogByPostIdAndUserIdAndCreatedAtBetween(Long postId, Long userId, LocalDate createdAt, LocalDate createdAt2);

    @Query(nativeQuery = true, value = "select " +
            "max(id) as id," +
            "max(user_id) as user_id," +
            "ul.post_id," +
            "max(event) as event," +
            "max(created_at) as created_at," +
            "max(ip_address) as ip_address" +
            " from user_log as ul where ul.user_id = :userId GROUP BY ul.post_id order by id desc limit 3")
    List<UserLog> findByUserId(Long userId);
}
