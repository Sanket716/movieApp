package com.sanket.movie.movieapp.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class DirectoryInitializer {

    @Value("${project.poster}")
    private String posterDirectoryPath;

    @PostConstruct
    public void createPosterDirectory() {
        File posterDirectory = new File(posterDirectoryPath);
        if (!posterDirectory.exists()) {
            boolean created = posterDirectory.mkdirs();
            if (created) {
                log.info(" Poster directory created: " + posterDirectoryPath);
            } else {
                log.error(" Failed to create poster directory: " + posterDirectoryPath);
            }
        } else {
            log.info(" Poster directory already exists: " + posterDirectoryPath);
        }
    }
}

