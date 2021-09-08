package ru.technoteinfo.site.controllers.api.v1;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.configuration.jwt.ConfirmEmailAuthenticationToken;
import ru.technoteinfo.site.configuration.jwt.JwtUtils;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.controllers.common.MailTextController;
import ru.technoteinfo.site.controllers.common.ValidatorController;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.pojo.response.JwtResponse;
import ru.technoteinfo.site.pojo.request.LoginRequest;
import ru.technoteinfo.site.pojo.request.RegisterRequest;
import ru.technoteinfo.site.repositories.RoleRepository;
import ru.technoteinfo.site.repositories.UserRepository;
import ru.technoteinfo.site.services.MailService;
import ru.technoteinfo.site.services.UserService;

import javax.mail.MessagingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    @Value("${site.domain}")
    private String domain;

    @Value("${site.security}")
    private String protocol;

    @Autowired
    private MailService mailService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private UserService userService;

//    private static Log log = LogFactory.getLog(MailService.class);

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> auth(@RequestBody LoginRequest loginRequest){
        JwtResponse jwtResponse = this.loginUser(loginRequest.getEmail(), loginRequest.getPassword(), null);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest registerRequest
    ){
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            JSONObject json = new JSONObject();
            json.put("status", "error");
            json.put("data", "Такая почта уже зарегистрирована");
            return ResponseEntity.badRequest().body(json.toString());
        }
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            JSONObject json = new JSONObject();
            json.put("status", "error");
            json.put("data", "Пароли не совпадают");
            return ResponseEntity.badRequest().body(json.toString());
        }
        User user = userService.registerUser(registerRequest.getEmail(), registerRequest.getPassword());
        if (user == null){
            JSONObject json = new JSONObject();
            json.put("status", "error");
            json.put("data", "Произошла ошибка при регистрации пользователя");
            return ResponseEntity.badRequest().body(json.toString());
        }
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info(String.format("Отправлена почта для восстановления пароля на почтовый ящик %s", user.getEmail()));
                    mailService.sendMail(user.getEmail(), "Подтверждение почты", MailTextController.confirmText(user, protocol+"://"+domain));
                } catch (MessagingException e) {
                    e.printStackTrace();
                    log.error("Не удалось отправить почту на ящик "+user.getEmail()+" по причине: "+e.getMessage());
                }
            }
        });
        JSONObject json = new JSONObject();
        json.put("status", "success");
        json.put("data", "Пользователь зарегистрирован");
        return  ResponseEntity.ok(json.toString());
    }

    @PostMapping("/activate-account")
    public ResponseEntity<?> confirmUserEmail(@RequestBody Map<String, String> body){
        String code = body.get("code");
        JSONObject json = new JSONObject();
        if (code == null){
            json.put("status", "error");
            json.put("message", "Код подтверждения не может быть пустым");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        User user = userService.checkActivate(code);

        if (user != null){
            JwtResponse jwtResponse = this.loginUser(user.getEmail(), user.getPassword(), user);
            json.put("status", "success");
            json.put("user", new JSONObject(jwtResponse));
            log.info("Пользователь активировал учетную запись через почту");
            return new ResponseEntity<>(json.toString(), HttpStatus.OK);
        }else{
            json.put("status", "error");
            json.put("message", "Такой пользователь не найден");
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_IMPLEMENTED);
        }

    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> getResetPasswordMail(@RequestParam Map<String, String> body){
        String email = ValidatorController.validateEmail(body.get("email"));
        JSONObject json = new JSONObject();
        if (email == null){
            json.put("status", "error");
            json.put("message", "Электронная почта не может быть пустым");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByEmail(email);
        if (user == null){
            json.put("status", "error");
            json.put("message", "Такой почтовый ящик не зарегистрирован");
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_IMPLEMENTED);
        }else{
            user.setAuthKey(CommonController.randomString(25));
            userRepository.save(user);
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info(String.format("Отправлена почта для восстановления пароля на почтовый ящик %s", user.getEmail()));
                        mailService.sendMail(user.getEmail(), "Восстановление пароля", MailTextController.resetPasswordText(user, protocol+"://"+domain));
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        log.error("Не удалось отправить почту на ящик "+user.getEmail()+" по причине: "+e.getMessage());
                    }
                }
            });
        }
        json.put("status", "success");
        json.put("message", "На Вашу почту отправлено письмо с сылкой для сброса пароля. Необходимо перейти по ней");
        return new ResponseEntity<>(json.toString(), HttpStatus.OK);
    }

    @RequestMapping("/reset-password-code")
    public ResponseEntity<?> checkCodeResetPassword(@RequestBody Map<String, String> body){
        String code = ValidatorController.validateCode(body.get("code"));
        JSONObject json = new JSONObject();
        if (code == null){
            json.put("status", "error");
            json.put("message", "Проверочный код не может быть пустым");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByAuthKey(code);
        if (user != null){
            json.put("status", "success");
            return new ResponseEntity<>(json.toString(), HttpStatus.OK);
        }else{
            json.put("status", "error");
            json.put("message", "Неверный проверочный код");
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @RequestMapping("/change-password-finish")
    public ResponseEntity<?> resetPasswordFinish(@RequestBody Map<String, String> body){
        String code = ValidatorController.validateCode(body.get("code"));
        String password = body.get("password");
        String confirmPassword = body.get("confirmPassword");
        JSONObject json = new JSONObject();
        if (password == null || confirmPassword == null){
            json.put("status", "error");
            json.put("message", "Пустой пароль или подтверждение пароля");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        if (!password.equals(confirmPassword)){
            json.put("status", "error");
            json.put("message", "Пароли не совпадают");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        if (code == null){
            json.put("status", "error");
            json.put("message", "Проверочный код не может быть пустым");
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByAuthKey(code);
        if (user != null){
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            json.put("status", "success");
            return new ResponseEntity<>(json.toString(), HttpStatus.OK);
        }else{
            json.put("status", "error");
            json.put("message", "Неверный проверочный код");
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    private JwtResponse loginUser(String email, String password, User user){
        Authentication authentication;

        if (user == null){
             authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }else{
            authentication = authenticationManager
                    .authenticate(new ConfirmEmailAuthenticationToken(email, user.getAuthKey()));
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        TechnoUserDetail userDetails = (TechnoUserDetail) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tokenDateExpire = formatter.format(new Date((new Date()).getTime() + jwtExpirationMs));
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                tokenDateExpire
        );
    }

}
