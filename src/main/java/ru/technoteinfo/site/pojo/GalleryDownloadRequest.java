package ru.technoteinfo.site.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryDownloadRequest {
    public int resultWidth;
    public int resultHeigth;
    public int resWidth; //размер картинки на странице для вычисления масштаба от оригинальной картинки
    public int resHeigth; //размер картинки на странице для вычисления масштаба от оригинальной картинки
    public int boxWidth; //область выделения
    public int boxHeight; //область выделения
    public int x1;
    public int y1;

    public GalleryDownloadRequest(int width, int heigth){
        this.resultWidth = width;
        this.resultHeigth = heigth;
        this.resWidth = 0;
        this.resHeigth = 0;
        this.boxWidth = 0;
        this.boxHeight = 0;
        this.x1 = 0;
        this.y1 = 0;
    }

}
