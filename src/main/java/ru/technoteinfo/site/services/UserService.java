package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.GenderEnum;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Modifying
    public boolean saveProfile(User user, User body){
        user.setFirstName(body.getFirstName());
        user.setLastName(user.getLastName());
        user.setAbout(body.getAbout());
        user.setGender(GenderEnum.valueOf(body.getGender().name()));
        user.setBirth(body.getBirth());
        userRepository.save(user);
        return true;
    }
}

