package ru.technoteinfo.site.services;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.technoteinfo.site.controllers.common.CommonController;
import ru.technoteinfo.site.repositories.impl.PostsRepoImpl;

@Service
@NoArgsConstructor
@Slf4j
public class NoteService extends DetailService {

    @Autowired
    private PostsRepoImpl postsRepo;

    @Autowired
    private CommonController common;

}
