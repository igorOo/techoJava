package ru.technoteinfo.site.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String translit;
    private List<CategoryResponse> childrens;

    public CategoryResponse(Long id, String name, String translit){
        this.id = id;
        this.name = name;
        this.translit = translit;
    }
}
