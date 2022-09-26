package org.yolkin.service;

import org.yolkin.dto.FileCreationDTO;
import org.yolkin.dto.FileDTO;
import org.yolkin.dto.mapper.FileMapper;
import org.yolkin.model.EventEntity;
import org.yolkin.model.FileEntity;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.yolkin.util.EventMaker.makeCreateFileEvent;

public class FileService {
    private final FileRepository fileRepository;
    private final EventRepository eventRepository;

    public FileService() {
        fileRepository = new HibernateFileRepositoryImpl();
        eventRepository = new HibernateEventRepositoryImpl();
    }

    public FileService(FileRepository fileRepository, EventRepository eventRepository) {
        this.fileRepository = fileRepository;
        this.eventRepository = eventRepository;
    }

    public List<FileEntity> getAll() {
        return fileRepository.getAll();
    }

    public FileDTO create(FileCreationDTO fileCreationDTO) {
        FileEntity fileEntity = FileMapper.toFile(fileCreationDTO);

        try {
            File uploadingDirectory = new java.io.File(fileCreationDTO.getPathForUploading());
            if (!uploadingDirectory.exists()) {
                if (!uploadingDirectory.mkdir()) {
                    return null;
                }
            }
            fileCreationDTO.getFileItem().write(new File(fileCreationDTO.getFilePath()));
        } catch (Exception e) {
            return null;
        }

        FileEntity fileWithId = fileRepository.create(fileEntity);

        if (fileWithId == null) {
            fileCreationDTO.getFileItem().delete();
            return null;
        }

        FileDTO fileDTO = FileMapper.toFileDTO(fileWithId);
        makeCreateFileEvent(fileWithId, fileCreationDTO.getUser(), eventRepository);

        return fileDTO;
    }

    public FileDTO getById(Long id) {
        FileEntity fileFromRepo = fileRepository.getById(id);

        if (fileFromRepo == null) {
            return null;
        }

        List<EventEntity> events = eventRepository.getAll().stream()
                .filter((event -> event.getFile().equals(fileFromRepo)))
                .collect(Collectors.toList());

        FileDTO fileDTO = FileMapper.toFileDTO(fileFromRepo);
        fileDTO.setEvents(events);

        return fileDTO;
    }

    public FileEntity update(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) {
//        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, userRepository, req, resp, PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE, mappingUrl);
//
//        if (helper.fileServiceUpdateRequestIsCorrect()) {
//            return helper.updateFile();
//        } else {
            return null;
//        }
}

    public void delete(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) {
//        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, userRepository, req, resp, mappingUrl);
//
//        if (helper.fileServiceDeleteRequestIsCorrect()) {
//            helper.deleteFile();
//        }
    }
}