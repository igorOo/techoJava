package ru.technoteinfo.site.services;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Gallery;
import ru.technoteinfo.site.pojo.response.GalleryResponse;
import ru.technoteinfo.site.repositories.GalleryRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
@NoArgsConstructor
public class GalleryService {
    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private CommonController commonController;

    private static final Logger LOGGER = LoggerFactory.getLogger(GalleryService.class.getName());

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

    public HashMap<String, List<String>> getListResolutions(String resolution){
        String[] imageResolution = resolution.split("x");
        HashMap<String, List<String>> result = new HashMap<>();
        Properties properties = new Properties();
        try {
            File file = ResourceUtils.getFile("classpath:resolutions.properties");
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        Iterator<?> propertyNames = properties.propertyNames().asIterator();

        String[] item;
        while(propertyNames.hasNext()){
            item = propertyNames.next().toString().split("\\.");
            result.put(item[0].equals("smartphone")?"Смартфоны":item[0], new ArrayList<>());
        }
        propertyNames = properties.propertyNames().asIterator();
        while(propertyNames.hasNext()){
            item = propertyNames.next().toString().split("\\.");
            String[] intItem = item[1].toLowerCase(Locale.ROOT).split("x");
            if ((Integer.parseInt(intItem[0]) <= Integer.parseInt(imageResolution[0])) &&
                    (Integer.parseInt(intItem[1]) <= Integer.parseInt(imageResolution[1]))
            ){
                List<String> value = result.get(item[0].equals("smartphone")?"Смартфоны":item[0]);
                value.add(item[1]);
                result.put(item[0].equals("smartphone")?"Смартфоны":item[0], value);
            }

        }

        return result;
    }
}
