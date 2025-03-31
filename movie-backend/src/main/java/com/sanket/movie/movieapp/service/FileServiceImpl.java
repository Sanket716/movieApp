package com.sanket.movie.movieapp.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(String path, MultipartFile multipartFile) throws IOException {
        try {
            //Get the file name
            String fileName = multipartFile.getOriginalFilename();
            //Get the file path
            String filePath = path + File.separator + fileName;
            // create a file object
            File posterFile = new File(path);
            if (!posterFile.exists()) {
                posterFile.mkdir();
            }
            Files.copy(multipartFile.getInputStream(), Paths.get(filePath));
            return fileName;
        }
        catch (Exception e)
        {
            log.error("An exception occurred: ", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public InputStream getResourceFile(String path, String name) throws FileNotFoundException {
        String filePath = path + File.separator + name;
        return new FileInputStream(filePath);
    }
}
