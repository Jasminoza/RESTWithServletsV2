package org.yolkin.dto;

import org.yolkin.model.UserEntity;

public class FileCreationDTO {
    private String name;
    private String filePath;
    private UserEntity user;

    public FileCreationDTO(String name, String filePath, UserEntity user) {
        this.name = name;
        this.filePath = filePath;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
