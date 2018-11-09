package com.youngbrains.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class BookStorageProperties {
    private String uploadDir;
    private String coverUploadDir;

    public String getCoverUploadDir() {
        return coverUploadDir;
    }

    public void setCoverUploadDir(String coverUploadDir) {
        this.coverUploadDir = coverUploadDir;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
