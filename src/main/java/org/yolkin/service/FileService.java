package org.yolkin.service;

import org.yolkin.model.FileEntity;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    static final int MAX_FILE_SIZE = 100 * 1024;
    static final int MAX_MEMORY_SIZE = 100 * 1024;
    private final String PATH_FOR_UPLOADING = "src/main/webapp/uploads";

    public FileService() {
        fileRepository = new HibernateFileRepositoryImpl();
        userRepository = new HibernateUserRepositoryImpl();
        eventRepository = new HibernateEventRepositoryImpl();
    }

    public FileService(FileRepository fileRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public List<FileEntity> getAll() {
        return fileRepository.getAll();
    }

    public FileEntity create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, userRepository, req, resp, PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);
//
//        if (helper.fileServiceCreateRequestIsCorrect()) {
//            return helper.createFile();
//        } else {
            return null;
//        }
    }

    public FileEntity getById(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
//        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, req, resp, mappingUrl);
//
//        if (helper.fileServiceGetByIdRequestIsCorrect()) {
//            return helper.getFileById();
//        } else {
            return null;
//        }
    }

    public FileEntity update(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
//        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, userRepository, req, resp, PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE, mappingUrl);
//
//        if (helper.fileServiceUpdateRequestIsCorrect()) {
//            return helper.updateFile();
//        } else {
            return null;
//        }
}

    public void delete(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
//        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, userRepository, req, resp, mappingUrl);
//
//        if (helper.fileServiceDeleteRequestIsCorrect()) {
//            helper.deleteFile();
//        }
    }
}