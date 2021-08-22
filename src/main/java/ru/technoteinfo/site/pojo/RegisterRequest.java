package ru.technoteinfo.site.pojo;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    private String email;
    private Set<String> roles;
    private String password;
    private String confirmPassword;

}
