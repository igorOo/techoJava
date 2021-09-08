package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
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

@Service
@NoArgsConstructor
public class MainService {

    @Autowired
    private CategoryRepo categoryRepo;


    public String createMenu(){
        JSONArray json = new JSONArray();
        json.put(new JSONObject().put("label", "Главная").put("url", "/"));
        json.put(new JSONObject().put("label", "Новости").put("url", "/news"));
        json.put(new JSONObject().put("label", "Статьи").put("url", "/notes"));
        json.put(new JSONObject().put("label", "Обои на рабочий стол").put("url", "/gallery"));
        json.put(new JSONObject().put("label", "Архив").put("url", "/archive"));

        this.addSubMenu(json.getJSONObject(1), 1L);
        this.addSubMenu(json.getJSONObject(2), 2L);
        this.addSubMenu(json.getJSONObject(3), 3L);
        try {
            File file = new File(System.getProperty("user.dir") + "/config/mainMenu.json");
            Path path = Paths.get(file.getAbsolutePath());
            if (!file.exists()) {
                Path parentPath = path.getParent();
                if (!Files.exists(parentPath)) {
                    Files.createDirectory(parentPath);
                }
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            final PrintStream printStream = new PrintStream(os);
            printStream.println(json.toString());
            printStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return json.toString();
    }

    public JSONObject addSubMenu(JSONObject parentMenu, Long typeCategory){
        List<Category> list = categoryRepo.findCategoryByTypePost(typeCategory);
        List<CategoryResponse> resultList = new ArrayList<>();
        for (Category item: list){
            resultList.add(new CategoryResponse(item.getId(), item.getName(), item.getTranslit()));
        }
        parentMenu.put("children", resultList);
        return parentMenu;
    }
}
