package ru.technoteinfo.site.pojo.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
