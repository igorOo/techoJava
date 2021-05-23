package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.entities.Gallery;
import ru.technoteinfo.site.pojo.GalleryResponse;
import ru.technoteinfo.site.services.GalleryService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/gallery")
@CrossOrigin(origins = "*")
public class GalleryController {
    @Autowired
    private GalleryService galleryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> index(
            @RequestParam(name = "page", required = false) Integer page
    ){
        final int pageSize = 9;
        if (page == null){
            page = 0;
        }else{
            page--;
        }
        HashMap<String, Object> result = new HashMap<>();
        Pageable sort = PageRequest.of(page, pageSize, Sort.by("dateEdit").descending());
        try {
            result = galleryService.getListImages(sort);
        }catch (Exception e){
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/category/{translit}", method = RequestMethod.GET)
    public ResponseEntity<?> category(
            @PathVariable(name = "translit") String category,
            @RequestParam(name = "page", required = false) Integer page
    ){
        final int pageSize = 9;
        if (page == null){
            page = 0;
        }else{
            page--;
        }
        HashMap<String, Object> result = new HashMap<>();
        Pageable sort = PageRequest.of(page, pageSize, Sort.by("dateEdit").descending());
        try {
            result = galleryService.getListImagesInCategory (category, sort);
        }catch (Exception e){
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{translit}", method = RequestMethod.GET)
    public ResponseEntity<?> detail(
            @PathVariable(name = "translit") String translit
    ){
        GalleryResponse image = galleryService.getDetailGallery(translit);
        if (image != null){
            HashMap<String, List<String>> resolutions = galleryService.getListResolutions(image.getResolution());
            HashMap<String, Object> result = new HashMap<>();
            result.put("image", image);
            result.put("resolutions", resolutions);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
