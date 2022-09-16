package org.yolkin.service;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yolkin.model.Event;
import org.yolkin.model.File;
import org.yolkin.model.User;
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
        ServiceHelper helper = new ServiceHelper();
        File file = null;

        String idFromRequest = req.getHeader("user_id");

        if (idFromRequest == null || idFromRequest.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "User id can't be null");
            return null;
        }

        Long userIdFromRequest;

        try {
            userIdFromRequest = Long.valueOf(idFromRequest);
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect user id.");
            return null;
        }

        User user = userRepository.getById(userIdFromRequest);

        if (user == null) {
            resp.sendError(SC_NOT_FOUND, "There is no user with such id.");
            return null;
        }

        ServletFileUpload uploader = helper.setupUploader(PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);
        try {
            Iterator<FileItem> iterator = uploader.parseRequest(req).iterator();
            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                Date date = new Date();

                if (!fileItem.isFormField()) {
                    java.io.File realFile = helper.getFileFromRequest(fileItem, PATH_FOR_UPLOADING, date);
                    try {
                        File fileForDB = new File();
                        fileForDB.setName(realFile.getName());
                        fileForDB.setDateOfUploading(date);
                        fileForDB.setFilepath(realFile.getPath());

                        file = fileRepository.create(fileForDB);
                        fileItem.write(realFile);

                        Event event = new Event();
//                        event.setUser(user);
//                        event.setFile(fileForDB);
                        eventRepository.create(event);
                        resp.setStatus(SC_CREATED);

                    } catch (Exception e) {
                        resp.sendError(SC_NOT_IMPLEMENTED, "Can't save file on hard drive");
                        return null;
                    }
                }
            }
        } catch (FileUploadException e) {
            resp.sendError(SC_NOT_ACCEPTABLE, "Can't upload file or size of all files exceeds " + MAX_FILE_SIZE / 1024 + " kb.");
        }
        return file;
    }

    public File getById(String id, HttpServletResponse resp) throws IOException {
        File file = null;

        try {
            Long idFromRequest = Long.valueOf(id);
            file = fileRepository.getById(idFromRequest);

            if (file == null) {
                resp.sendError(SC_NOT_FOUND, "There is no file with such id");
            }
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect file id");
        }
        return file;
    }

    public File update(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        ServiceHelper helper = new ServiceHelper();

        File file = null;

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "file id can't be null");
            return null;
        } else {
            try {
                Long idFromRequest = Long.valueOf(id);
                file = fileRepository.getById(idFromRequest);

                if (file == null) {
                    resp.sendError(SC_NOT_FOUND, "There is no file with such id");
                    return null;
                } else {
                    ServletFileUpload uploader = helper.setupUploader(PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);
                    try {
                        Iterator<FileItem> iterator = uploader.parseRequest(req).iterator();
                        while (iterator.hasNext()) {
                            FileItem fileItem = iterator.next();
                            Date date = new Date();

                            if (!fileItem.isFormField()) {
                                java.io.File realFile = helper.getFileFromRequest(fileItem, PATH_FOR_UPLOADING, date);
                                try {
                                    File fileForDB = new File();
                                    fileForDB.setName(realFile.getName());
                                    fileForDB.setDateOfUploading(date);
                                    fileForDB.setFilepath(realFile.getPath());

                                    file = fileRepository.update(fileForDB);
                                    fileItem.write(realFile);

                                    resp.setStatus(SC_CREATED);

                                } catch (Exception e) {
                                    resp.sendError(SC_NOT_IMPLEMENTED, "Can't save file on hard drive");
                                    return null;
                                }
                            }
                        }
                    } catch (FileUploadException e) {
                        resp.sendError(SC_NOT_ACCEPTABLE, "Can't upload file or size of all files exceeds " + MAX_FILE_SIZE / 1024 + " kb.");
                    }
                }
            } catch (NumberFormatException e) {
                resp.sendError(SC_BAD_REQUEST, "Incorrect file id");
            }
        }
        return file;
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp, String mappingUrl) throws IOException {
        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "File id can't be null");
        } else {
            try {
                Long idFromRequest = Long.valueOf(id);
                File file = fileRepository.getById(idFromRequest);

                if (file == null) {
                    resp.sendError(SC_NOT_FOUND, "There is no file with such id");
                } else {
                    fileRepository.delete(idFromRequest);
                    resp.setStatus(SC_OK);
                }
            } catch (NumberFormatException e) {
                resp.sendError(SC_BAD_REQUEST, "Incorrect file id");
            }
        }
    }
}