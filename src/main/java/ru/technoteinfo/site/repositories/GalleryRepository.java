package ru.technoteinfo.site.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.technoteinfo.site.entities.Gallery;
import ru.technoteinfo.site.pojo.GalleryResponse;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    Page<Gallery> findByCategory_Translit(String category, Pageable pageable);
    Gallery findFirstByTranslit(String translit);
}
