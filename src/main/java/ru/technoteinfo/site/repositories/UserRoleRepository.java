package ru.technoteinfo.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.technoteinfo.site.entities.UserRoles;

public interface UserRoleRepository extends JpaRepository<UserRoles, Long> {
}
