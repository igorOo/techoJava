package ru.technoteinfo.site.services;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.Subscribe;
import ru.technoteinfo.site.repositories.SubscribeRepo;

import java.util.Date;

@Service
@NoArgsConstructor
public class SubscribeService {
    private SubscribeRepo subscribeRepo;

    @Autowired
    public void setSubscribeRepo(SubscribeRepo subscribeRepo) {
        this.subscribeRepo = subscribeRepo;
    }

    public void add(String email, String ip_address){
        Subscribe subscribe = new Subscribe();
        subscribe.setEmail(email);
        subscribe.setIp_address(ip_address);
        subscribe.setDate(new Date());
        subscribeRepo.save(subscribe);
    }
}
