package org.yolkin.service;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yolkin.model.Event;
import org.yolkin.model.File;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;
import org.yolkin.util.ServiceHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

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

    public List<File> getAll() {
        return fileRepository.getAll();
    }

    public File create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServiceHelper helper = new ServiceHelper(eventRepository, userRepository, req, resp, PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);

        if (helper.fileServiceCreateRequestIsCorrect()) {
            return helper.createFile();
        } else {
            return null;
        }
    }

    public File getById(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, req, resp, mappingUrl);

        if (helper.fileServiceGetByIdRequestIsCorrect()) {
            return helper.getFileById();
        } else {
            return null;
        }
    }

    public File update(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, userRepository, req, resp, mappingUrl);

        if (helper.fileServiceUpdateRequestIsCorrect()) {
            return helper.updateFile();
        } else {
            return null;
        }

//
//        ServletFileUpload uploader = helper.setupUploader(PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);
//        try {
//            Iterator<FileItem> iterator = uploader.parseRequest(req).iterator();
//            while (iterator.hasNext()) {
//                FileItem fileItem = iterator.next();
//                Date date = new Date();
//
//                if (!fileItem.isFormField()) {
//                    java.io.File realFile = helper.getFileFromRequest(fileItem, PATH_FOR_UPLOADING, date);
//                    try {
//                        File fileForDB = new File();
//                        fileForDB.setName(realFile.getName());
//                        fileForDB.setDateOfUploading(date);
//                        fileForDB.setFilepath(realFile.getPath());
//
//                        file = fileRepository.update(fileForDB);
//                        fileItem.write(realFile);
//
//                        Event event = new Event();
////                                    event.setEvent();// TODO::
//
//                        resp.setStatus(SC_CREATED);
//
//                    } catch (Exception e) {
//                        resp.sendError(SC_NOT_IMPLEMENTED, "Can't save file on hard drive");
//                        return null;
//                    }
//                }
//            }
//        } catch (FileUploadException e) {
//            resp.sendError(SC_NOT_ACCEPTABLE, "Can't upload file or size of all files exceeds " + MAX_FILE_SIZE / 1024 + " kb.");
//        }
//    }

}

    public void delete(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        ServiceHelper helper = new ServiceHelper(eventRepository, fileRepository, userRepository, req, resp, mappingUrl);

        if (helper.fileServiceDeleteRequestIsCorrect()) {
            helper.deleteFile();
        }
    }
}