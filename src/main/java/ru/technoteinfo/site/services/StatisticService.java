package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.entities.*;
import ru.technoteinfo.site.entities.Enums.EventEnum;
import ru.technoteinfo.site.entities.queriesmodels.TopPost;
import ru.technoteinfo.site.exceptions.CustomForbiddenException;
import ru.technoteinfo.site.repositories.StatisticPostReadRepo;
import ru.technoteinfo.site.repositories.UserLogRepository;
import ru.technoteinfo.site.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class StatisticService{
    @Autowired
    private StatisticPostReadRepo statisticPostReadRepo;

    @Autowired
    private UserLogRepository userLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonController commonController;

    public void savePostReadTime(StatisticPostRead body, String ip_address){
        body.setIpAddress(ip_address);
        body.setDateCreate(new Date());
        statisticPostReadRepo.save(body);
    }

    public void addViewPost(Long postId, String ipAddress){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken){
            return;
        }
        TechnoUserDetail user = (TechnoUserDetail) auth.getPrincipal();
        LocalDate beginDate = LocalDate.now();
        int countRows = userLogRepository.countUserLogByPostIdAndUserIdAndCreatedAtBetween(postId, user.getId(), beginDate, beginDate.plusDays(1));
        if (countRows == 0){
            UserLog userLog = new UserLog();
            userLog.setUserId(user.getId());
            userLog.setPostId(postId);
            userLog.setEvent(EventEnum.READ_POST);
            userLog.setIpAddress(ipAddress);
            userLogRepository.save(userLog);
        }
    }

    public List<TopPost> getLastViewPost() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken){
            throw new CustomForbiddenException("Доступ запрещен");
        }
        TechnoUserDetail user = (TechnoUserDetail) auth.getPrincipal();

        List<UserLog> logs = userLogRepository.findByUserId(user.getId() );
        List<TopPost> list = logs
                .stream()
                .map(item -> item.getPost().toTopPost())
                .collect(Collectors.toList());
        list = commonController.formatMeta(list);
        return list;
    }
}
