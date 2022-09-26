package org.yolkin.dto;

import org.apache.commons.fileupload.FileItem;
import org.yolkin.model.UserEntity;

public class FileCreationDTO {
    private String name;
    private String filePath;
    private UserEntity user;
    private FileItem fileItem;
    private String pathForUploading;

    public FileCreationDTO() {
    }

    public FileCreationDTO(String name, String filePath, UserEntity user, FileItem fileItem, String pathForUploading) {
        this.name = name;
        this.filePath = filePath;
        this.user = user;
        this.fileItem = fileItem;
        this.pathForUploading = pathForUploading;
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

    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public String getPathForUploading() {
        return pathForUploading;
    }

    public void setPathForUploading(String pathForUploading) {
        this.pathForUploading = pathForUploading;
    }
}
