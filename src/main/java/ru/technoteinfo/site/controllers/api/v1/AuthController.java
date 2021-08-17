package ru.technoteinfo.site.controllers.api.v1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.configuration.jwt.JwtUtils;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.controllers.common.MailTextController;
import ru.technoteinfo.site.entities.Roles;
import ru.technoteinfo.site.entities.RolesEnum;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.pojo.JwtResponse;
import ru.technoteinfo.site.pojo.LoginRequest;
import ru.technoteinfo.site.repositories.RoleRepository;
import ru.technoteinfo.site.repositories.UserRepository;
import ru.technoteinfo.site.services.MailService;

import javax.mail.MessagingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
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

    @Autowired
    private MailService mailService;

    @Autowired
    private TaskExecutor taskExecutor;

    private static Log log = LogFactory.getLog(MailService.class);

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> auth(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        TechnoUserDetail userDetails = (TechnoUserDetail) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tokenDateExpire = formatter.format(new Date((new Date()).getTime() + jwtExpirationMs));

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                tokenDateExpire
                ));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword
    ){
        if (userRepository.existsByEmail(email)){
            return ResponseEntity.badRequest().body("Такая почта уже зарегистрирована");
        }
        if (!password.equals(confirmPassword)){
            return ResponseEntity.badRequest().body("Пароли не совпадают");
        }
        List<Roles> roles = new ArrayList<>();
        Roles role = roleRepository.findByName(RolesEnum.ROLE_USER);
        roles.add(role);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAuth_key(CommonController.randomString(25));
        user.setRoles(roles);

        userRepository.save(user);
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mailService.sendMail(user.getEmail(), "Подтверждение почты", MailTextController.confirmText(user, domain));
                } catch (MessagingException e) {
                    e.printStackTrace();
                    log.error("Failed to send email to: "+user.getEmail()+" reason: "+e.getMessage());
                }
            }
        });
        return  ResponseEntity.ok("Пользователь зарегистрирован");
    }

    @GetMapping(value = "/testmail")
    public String testMail(){

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mailService.sendMail("igorek0_0@mail.ru", "hi", "Текст");
                } catch (MessagingException e) {
                    e.printStackTrace();
                    log.error("Failed to send email to: igorek0_0@mail.ru reason: "+e.getMessage());
                }
            }
        });
        return "ok";
    }
}
