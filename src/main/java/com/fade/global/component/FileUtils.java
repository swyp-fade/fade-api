package com.fade.global.component;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class FileUtils {
    private static final DateTimeFormatter dtfhFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/hh");

    public String generateFilepath() {
        return LocalDateTime.now().format(dtfhFormatter);
    }

    public String getRandomFilename() {
        return UUID.randomUUID().toString();
    }
}
