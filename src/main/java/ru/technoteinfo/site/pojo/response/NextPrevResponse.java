package ru.technoteinfo.site.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NextPrevResponse {
    private String name;
    private String url;
}
