package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.Category;
import ru.technoteinfo.site.repositories.CategoryRepo;

import java.util.List;

@Service
@NoArgsConstructor
public class NewsCategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CommonController common;

    public List<Category> getCategoryList(String translit){
        List<Category> list = categoryRepo.findByTranslitAndType_postOrderBySort(translit, "desc");
        return list;
    }

}
