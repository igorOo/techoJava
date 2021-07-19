package ru.technoteinfo.site.services.common;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class FilesService {

    public BufferedImage resizeImage(BufferedImage image, int width, int heigth){
        if (width<heigth){
            double scale = (double)width/heigth;
            double scaledWidth = (double) image.getHeight()*scale;
            int x1 = (image.getWidth() - (int)scaledWidth)/2;
            int y1 = 0;
            image = Scalr.crop(image, x1, y1, (int)scaledWidth, image.getHeight());
        }
        return Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, width, heigth, Scalr.OP_ANTIALIAS);
    }
}
