package com.sanket.movie.movieapp.controllers;

import com.sanket.movie.movieapp.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "*")
@Slf4j
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${project.poster}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestParam MultipartFile file ) throws IOException
    {
        String uploadFileName = fileService.uploadFile(path,file);
        return ResponseEntity.ok("File uploaded successfully with name " + uploadFileName);
    }

    @GetMapping("/{fileName}")
    public void serveFileName(@PathVariable String fileName, HttpServletResponse httpServletResponse) throws IOException
    {
        InputStream resourceFile = null;
        try {
            resourceFile = fileService.getResourceFile(path, fileName);
            Path filePath = Paths.get(path, fileName);

            // Determine content type dynamically
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Default fallback
            }

            httpServletResponse.setContentType(contentType);
            StreamUtils.copy(resourceFile, httpServletResponse.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("An exception occurred " + e);
        }
        finally {
            if(resourceFile!=null) {
                resourceFile.close();
            }
        }
    }
}
