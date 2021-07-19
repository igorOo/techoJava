package ru.technoteinfo.site.services;

import javassist.NotFoundException;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.util.InMemoryResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ru.technoteinfo.site.controllers.common.FilesController;
import ru.technoteinfo.site.entities.Gallery;
import ru.technoteinfo.site.pojo.GalleryDownloadRequest;
import ru.technoteinfo.site.repositories.GalleryRepository;
import ru.technoteinfo.site.services.common.FilesService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StorageService {

    @Value("${storage.download.path}")
    private String folderPath;

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private FilesService filesService;

    public Resource downloadToBrowser(String translit, GalleryDownloadRequest body) throws Exception, NotFoundException {
        Gallery image = galleryRepository.findFirstByTranslit(translit);
        if (image == null){
            throw new NotFoundException("Файл не найден");
        }
        try {
            Path file = Paths.get(folderPath + "/main/"+ image.getCategory().getId() + "/" + image.getFilename());

            Path resultImage;
            if (body.resWidth > 0 && body.resHeigth > 0){
                String resultImagePath = cropImage(file, body);
                resultImage = Paths.get(resultImagePath);
            }else{
                String resultImagePath = resizeImage(file, body.resultWidth, body.resultHeigth);
                resultImage = Paths.get(resultImagePath);
            }

            Resource resource = new UrlResource(resultImage.toUri());
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

    //TODO::запихать в отдельный поток
    public String resizeImage(Path file, int width, int heigth) throws IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        BufferedImage resultImage = filesService.resizeImage(ImageIO.read(file.toFile()), width, heigth);
        String fileName = tmpDir+UUID.randomUUID().toString()+".jpg";
        File savedFile = new File(fileName);
        if (ImageIO.write(resultImage, "jpg", savedFile)){
            return fileName;
        }else{
            throw new IOException("Не удалось создать новый файл");
        }
    }

    //TODO::запихать в отдельный поток
    public String cropImage(Path file, GalleryDownloadRequest body) throws IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        BufferedImage originalImage = ImageIO.read(file.toFile());
        int width = originalImage.getWidth();

        double scale = (double)width/body.resWidth;
        int x1 = (int)(body.x1 * scale);
        int y1 = (int)(body.y1 * scale);
        int selectWidth = (int)(body.boxWidth * scale);
        int selectHeight = (int)(body.boxHeight * scale);

        BufferedImage croppedImage = originalImage.getSubimage(x1,y1,selectWidth,selectHeight);
        String fileName = tmpDir+UUID.randomUUID().toString()+".jpg";
        File savedFile = new File(fileName);
        if (ImageIO.write(croppedImage, "jpg", savedFile)){
            return fileName;
        }else{
            throw new IOException("Не удалось создать новый файл");
        }
    }
}
