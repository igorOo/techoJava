package ru.technoteinfo.site.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.technoteinfo.site.services.StorageService;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/download")
public class DownloadController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/{translit}/{resolution}")
    public ResponseEntity<?> downloadFile(
            @PathVariable("translit") String translit,
            @PathVariable("resolution") String resolution
    ){
        Resource resource = null;
        try {
            resource = storageService.downloadToBrowser(translit, resolution);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
