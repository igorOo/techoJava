package ru.technoteinfo.site.pojo;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
