package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.technoteinfo.site.configuration.jwt.JwtUtils;
import ru.technoteinfo.site.entities.Roles;
import ru.technoteinfo.site.entities.RolesEnum;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.pojo.JwtResponse;
import ru.technoteinfo.site.pojo.LoginRequest;
import ru.technoteinfo.site.repositories.RoleRepository;
import ru.technoteinfo.site.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    public ResponseEntity<?> register(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName
    ){
        if (userRepository.existsByEmail(email)){
            return ResponseEntity.badRequest().body("Такая почта уже зарегистрирована");
        }
        List<Roles> roles = new ArrayList<>();
        Roles role = roleRepository.findByName(RolesEnum.ROLE_USER);
        roles.add(role);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(roles);

        userRepository.save(user);
        return  ResponseEntity.ok("Пользователь зарегистрирован");
    }
}
