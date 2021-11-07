package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.Category;
import ru.technoteinfo.site.pojo.response.CategoryResponse;
import ru.technoteinfo.site.repositories.CategoryRepo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class MainService {

    @Autowired
    private CategoryRepo categoryRepo;


//    public String createMenu(){
//        JSONArray json = new JSONArray();
//        json.add(new JSONObject().put("label", "Главная").put("url", "/"));
//        json.add(new JSONObject().put("label", "Новости").put("url", "/news"));
//        json.add(new JSONObject().put("label", "Статьи").put("url", "/notes"));
//        json.add(new JSONObject().put("label", "Обои на рабочий стол").put("url", "/gallery"));
//        json.add(new JSONObject().put("label", "Архив").put("url", "/archive"));
//
//        this.addSubMenu(json.getJSONObject(1), 1L);
//        this.addSubMenu(json.getJSONObject(2), 2L);
//        this.addSubMenu(json.getJSONObject(3), 3L);
//        try {
//            File file = new File(System.getProperty("user.dir") + "/config/mainMenu.json");
//            Path path = Paths.get(file.getAbsolutePath());
//            if (!file.exists()) {
//                Path parentPath = path.getParent();
//                if (!Files.exists(parentPath)) {
//                    Files.createDirectory(parentPath);
//                }
//                file.createNewFile();
//            }
//            OutputStream os = new FileOutputStream(file);
//            final PrintStream printStream = new PrintStream(os);
//            printStream.println(json.toString());
//            printStream.close();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        return json.toString();
//    }
//
//    public JSONObject addSubMenu(JSONObject parentMenu, Long typeCategory){
//        List<Category> list = categoryRepo.findCategoryByTypePost(typeCategory);
//        List<CategoryResponse> resultList = new ArrayList<>();
//        for (Category item: list){
//            resultList.add(new CategoryResponse(item.getId(), item.getName(), item.getTranslit()));
//        }
//        parentMenu.put("children", resultList);
//        return parentMenu;
//    }

    public List<CategoryResponse> getMainMenu(){
        List<Category> categories = categoryRepo.findByParentCategoryIsNull();
        List<CategoryResponse> list = new ArrayList<>();
        list.add(new CategoryResponse(1L, "Главная", ""));
        if (categories != null){
            for (Category item: categories){
                list.add(item.toCategoryResponse());
            }
        }
        return list;
    }

    public List<CategoryResponse> getSubmenuFromMenu(String slug){
        List<Category> menu = categoryRepo.findByParentCategoryIsNull();
        List<CategoryResponse> list = new ArrayList<>();
        if (menu != null){
            for(Category item: menu){
                if (item.getTranslit().equals(slug)){
                    for (Category children: item.getChildrens()){
                        list.add(children.toCategoryResponse());
                    }
                }
            }
        }
        return list;
    }
}
