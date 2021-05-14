package ru.technoteinfo.site.services;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Gallery;
import ru.technoteinfo.site.pojo.GalleryResponse;
import ru.technoteinfo.site.repositories.GalleryRepository;

import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Data
@NoArgsConstructor
public class GalleryService {
    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private CommonController commonController;

    public HashMap<String, Object> getListImages(Pageable pageable){
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Integer> pages = new HashMap<>();
        Page<Gallery> page = galleryRepository.findAll(pageable);
        pages.put("currentPage", page.getNumber()+1);
        pages.put("lastPage", page.getTotalPages());

        result.put("data", page.getContent().stream()
                .map(item -> item.toGalleryResponse())
                .peek(item -> commonController.formatGalleryItem(item, false))
                .collect(Collectors.toList()));
        result.put("pages", pages);
        return result;
    }

    public HashMap<String, Object> getListImagesInCategory(String category, Pageable pageable){
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, Integer> pages = new HashMap<>();
        Page<Gallery> page = galleryRepository.findByCategory_Translit(category, pageable);
        pages.put("currentPage", page.getNumber()+1);
        pages.put("lastPage", page.getTotalPages());

        result.put("data", page.getContent().stream()
                .map(item -> item.toGalleryResponse())
                .peek(item -> commonController.formatGalleryItem(item, false))
                .collect(Collectors.toList()));
        result.put("pages", pages);
        return result;
    }

    public GalleryResponse getDetailGallery(String translit){
        Gallery gallery = galleryRepository.findFirstByTranslit(translit);
        GalleryResponse result = null;
        if (gallery != null){
            result = gallery.toGalleryResponse();
            commonController.formatGalleryItem(result, true);
        }
        return result;
    }
}
