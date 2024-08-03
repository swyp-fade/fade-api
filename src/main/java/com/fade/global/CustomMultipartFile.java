package com.fade.global;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
public class CustomMultipartFile implements MultipartFile {
    private final byte[] data;
    private String contentType;
    private String name;
    private String originalFilename;

    public CustomMultipartFile(byte[] data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.length == 0;
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return data;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try(FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(data);
        }
    }
}
