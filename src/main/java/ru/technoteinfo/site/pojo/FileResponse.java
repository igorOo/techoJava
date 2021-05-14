package ru.technoteinfo.site.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private String name;
    private String uri;
    private String type;
    private long size;
    private String resolution;
}
