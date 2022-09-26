package org.yolkin.dto.mapper;

import org.yolkin.dto.FileCreationDTO;
import org.yolkin.dto.FileDTO;
import org.yolkin.model.FileEntity;

public class FileMapper {
    public static FileEntity toFile(FileCreationDTO fileCreationDTO) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileCreationDTO.getName());
        fileEntity.setFilePath(fileCreationDTO.getFilePath());
        return fileEntity;
    }

    public static FileDTO toFileDTO(FileEntity fileEntity) {
        return new FileDTO(fileEntity.getId(), fileEntity.getName(), fileEntity.getFilePath(), fileEntity.getEvents());
    }

    public static FileEntity toFile(FileDTO fileDTO) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(fileDTO.getId());
        fileEntity.setName(fileDTO.getName());
        fileEntity.setFilePath(fileDTO.getFilePath());
        fileEntity.setEvents(fileDTO.getEvents());
        return fileEntity;
    }
}
