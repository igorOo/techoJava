package ru.technoteinfo.site.services;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.Gallery;
import ru.technoteinfo.site.repositories.GalleryRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

            BufferedImage resizedImage = resizeImage(file, resolut[0], resolut[1]);

            Resource resource = new UrlResource(resizedImage.toString());
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

    public BufferedImage resizeImage(Path file, int width, int heigth){

    }

    public BufferedImage cropImage(Path file, int toWidth, int toHeigth) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.toFile());
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // Coordinates of the image's middle
        int xc = (width - toWidth) / 2;
        int yc = (height - toHeigth) / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc,
                yc,
                toWidth, // widht
                toHeigth // height
        );
        return croppedImage;
    }
}
