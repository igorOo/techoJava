package ru.technoteinfo.site.services;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.Gallery;
import ru.technoteinfo.site.repositories.GalleryRepository;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class StorageService {

    @Value("${storage.download.path}")
    private String folderPath;

    @Autowired
    private GalleryRepository galleryRepository;

    public Resource downloadToBrowser(String translit, String resolution) throws Exception, NotFoundException {
        int[] resolut = Arrays.stream(resolution.split("x")).mapToInt(Integer::parseInt).toArray();
        Gallery image = galleryRepository.findFirstByTranslit(translit);
        if (image == null){
            throw new NotFoundException("Файл не найден");
        }
        try {
            Path file = Paths.get(folderPath + "/main/"+ image.getCategory().getId() + "/" + image.getFilename());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Файл не найден: " + translit);
            }
        }
        catch (MalformedURLException | FileNotFoundException e) {
            throw new Exception("Файл не найден: " + translit, e);
        }
    }
}
