package ru.technoteinfo.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.technoteinfo.site.entities.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles findByName(String name);
}
