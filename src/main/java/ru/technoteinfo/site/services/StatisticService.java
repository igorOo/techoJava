package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.entities.StatisticPostRead;
import ru.technoteinfo.site.repositories.StatisticPostReadRepo;

import javax.servlet.http.HttpServletRequest;

@Service
@NoArgsConstructor
public class StatisticService{
    @Autowired
    private StatisticPostReadRepo statisticPostReadRepo;

    public void savePostReadTime(StatisticPostRead body, String ip_address){
        body.setIpAddress(ip_address);
        statisticPostReadRepo.save(body);
    }
}
