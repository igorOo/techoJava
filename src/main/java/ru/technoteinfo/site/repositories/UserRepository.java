package ru.technoteinfo.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.technoteinfo.site.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String username);
    User findByEmail(String email);
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);

    User findByAuthKey(String code);
}
