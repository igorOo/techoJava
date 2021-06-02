package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.repositories.UserRepository;
import ru.technoteinfo.site.services.UserService;

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
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> edit(
            @RequestBody User body
    ){
        TechnoUserDetail auth = (TechnoUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(auth.getEmail());
        if (user == null){
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.FORBIDDEN);
        }
        try {
            userService.saveProfile(user, body);
            HashMap<String, String> result = new HashMap<>();
            result.put("status", "success");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Произошла ошибка при сохранении профиля", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
