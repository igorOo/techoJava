package ru.technoteinfo.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.technoteinfo.site.controllers.common.FilesController;
import ru.technoteinfo.site.entities.GenderEnum;
import ru.technoteinfo.site.entities.User;
import ru.technoteinfo.site.repositories.UserRepository;
import ru.technoteinfo.site.services.common.FilesService;
import ru.technoteinfo.site.services.common.SftpService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilesService filesService;

    @Autowired
    private SftpService sftpService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Modifying
    public boolean saveProfile(User user, String firstName, String lastName, String gender, Date birth, String about){
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAbout(about);
        user.setGender(GenderEnum.valueOf(gender));
        user.setBirth(birth);
        userRepository.save(user);
        return true;
    }

    @Modifying
    public boolean saveAvatar(MultipartFile avatar, User user, String pathFolder){
        String typeFile = avatar.getContentType();
        try {
            String tmpDir = System.getProperty("java.io.tmpdir");
            File file = new File(tmpDir + user.getId()+".jpg");

            BufferedImage originalImage = ImageIO.read(avatar.getInputStream());
            BufferedImage resizedImage = filesService.resizeImage(originalImage, 150, 150);
            if (ImageIO.write(resizedImage, "jpg", file)){
                String result = sftpService.uploadFile(file.toString(), "web/images/profiles/"+user.getId()+".jpg");
                if (result != null){
                    user.setAvatar(result);
                    userRepository.save(user);
                    return true;
                }
                return false;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Modifying
    public boolean updatePassword(User user, String password){
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }
}

