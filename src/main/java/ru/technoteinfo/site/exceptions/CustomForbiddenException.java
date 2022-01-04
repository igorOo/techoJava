package ru.technoteinfo.site.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CustomForbiddenException extends RuntimeException{

    public CustomForbiddenException(String message){
        super(message);
    }
}
