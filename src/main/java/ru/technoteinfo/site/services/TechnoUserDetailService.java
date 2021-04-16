package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.TechnoUserDetail;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.repositories.UserRepository;

import javax.transaction.Transactional;

@Service
public class TechnoUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public TechnoUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new TechnoUserDetail(user);
    }
}
