package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.repositories.UserRepository;
import ru.technoteinfo.site.services.StorageService;
import ru.technoteinfo.site.services.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommonController commonController;

    @Autowired
    UserService userService;

    @RequestMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> profile(
            @RequestHeader("Authorization") String autorization
    ){
        TechnoUserDetail auth = (TechnoUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(auth.getEmail());
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.FORBIDDEN);
        }
        user.setAvatar(commonController.getAvatarUrl(user.getAvatar()));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/profile/edit",
            method = RequestMethod.POST,
            consumes = {"multipart/form-data"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> edit(
            @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "gender", required = true) String gender,
            @RequestParam(value = "birth", required = false) Date birth,
            @RequestParam(value = "about", required = false) String about,
            @RequestParam(value = "avatar", required = false) MultipartFile file
    ){
        TechnoUserDetail auth = (TechnoUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(auth.getEmail());
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.FORBIDDEN);
        }
        try {
            userService.saveProfile(user, firstName, lastName, gender, birth, about);
            if (file != null){
                userService.saveAvatar(file, user, "");
            }
            HashMap<String, String> result = new HashMap<>();
            result.put("status", "success");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Произошла ошибка при сохранении профиля", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/profile/change-password",
            method = RequestMethod.POST,
            consumes = {"multipart/form-data"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> changePassword(
            @RequestParam(value = "password") String password,
            @RequestParam(value = "confirmPassword") String confirnPassword
    ){
        TechnoUserDetail auth = (TechnoUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(auth.getEmail());
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.FORBIDDEN);
        }
        if (!password.equals(confirnPassword)){
            return new ResponseEntity<>("Пароли не совпадают", HttpStatus.NOT_IMPLEMENTED);
        }
        try{
            userService.updatePassword(user, password);
            HashMap<String, String> result = new HashMap<>();
            result.put("status", "success");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Произошла ошибка при смене пароля", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor is a custom date editor
    }
}
