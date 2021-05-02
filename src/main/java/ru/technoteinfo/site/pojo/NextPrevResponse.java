package ru.technoteinfo.site.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NextPrevResponse {
    private String name;
    private String url;
}
