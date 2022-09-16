package org.yolkin.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yolkin.model.Event;
import org.yolkin.model.User;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static javax.servlet.http.HttpServletResponse.*;

public class ServiceHelper {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private HttpServletResponse resp;
    private HttpServletRequest req;
    private Long userIdFromRequest;
    private User userFromRepo;
    private String mappingUrl;
    private String idFromUrl;

    public ServiceHelper(EventRepository eventRepository, UserRepository userRepository, HttpServletResponse resp, HttpServletRequest req, String mappingUrl) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fileRepository = null;
        this.resp = resp;
        this.req = req;
        this.mappingUrl = mappingUrl;
    }

    public ServiceHelper(EventRepository eventRepository, UserRepository userRepository, HttpServletResponse resp, HttpServletRequest req) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fileRepository = null;
        this.resp = resp;
        this.req = req;
        this.mappingUrl = null;
    }

    public ServletFileUpload setupUploader(String PATH_FOR_UPLOADING, int MAX_MEMORY_SIZE, int MAX_FILE_SIZE) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(new java.io.File(PATH_FOR_UPLOADING));
        diskFileItemFactory.setSizeThreshold(MAX_MEMORY_SIZE);

        ServletFileUpload uploader = new ServletFileUpload(diskFileItemFactory);
        uploader.setSizeMax(MAX_FILE_SIZE);
        uploader.setFileSizeMax(MAX_MEMORY_SIZE);
        return uploader;
    }

    public java.io.File getFileFromRequest(FileItem fileItem, String PATH_FOR_UPLOADING, Date date) {
        java.io.File realFile;

        String fileName = fileItem.getName();
        if (fileName.lastIndexOf("\\") >= 0) {
            realFile = new java.io.File(PATH_FOR_UPLOADING + File.separator + date + " " +
                    fileName.substring(fileName.lastIndexOf("\\")));
        } else {
            realFile = new java.io.File(PATH_FOR_UPLOADING + File.separator + date + " " +
                    fileName.substring(fileName.lastIndexOf("\\") + 1));
        }

        return realFile;
    }

    public void makeCreateUserEvent(User user){
        Event event = new Event();
        event.setEvent("[" + new Date() + "] " + "INFO:  User{id: " + user.getId() + " name: " + user.getName() + "} was created.");
        eventRepository.create(event);
    }

    public void makeUpdateUserEvent(User user) {
        Event event = new Event();
        event.setEvent("[" + new Date() + "] " + "INFO:  User{id: " + user.getId() + " name: " + user.getName() + "} was updated.");
        eventRepository.create(event);
    }

    public void makeDeleteUserEvent(User user) {
        Event event = new Event();
        event.setEvent("[" + new Date() + "] " + "INFO:  User{id: " + user.getId() + " name: " + user.getName() + "} was deleted.");
        eventRepository.create(event);
    }

    public User createUser() {
        User user = new User();
        user.setName(req.getHeader("username"));
        user = userRepository.create(user);
        makeCreateUserEvent(user);
        resp.setStatus(SC_CREATED);
        return user;
    }

    public boolean userServiceCreateRequestIsCorrect() throws IOException {
        String headerName = "username";
        return headerNotBlank(headerName);
    }

    private boolean headerNotBlank(String headerName) throws IOException {
        String headerValue = req.getHeader(headerName);

        if (headerValue == null || headerValue.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, headerName + " can't be null");
            return false;
        }
        return true;
    }

    public boolean userServiceGetByIdRequestIsCorrect(String id) throws IOException {
        return idFromUrlIsCorrect(id) && userWasFound();
    }

    private boolean idFromUrlIsCorrect(String id) throws IOException {
        try {
            userIdFromRequest = Long.valueOf(id);
            return true;
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect id");
            return false;
        }
    }

    public User getUserById() {
        return userFromRepo;
    }

    private boolean userWasFound() throws IOException {
        userFromRepo = userRepository.getById(userIdFromRequest);

        if (userFromRepo == null) {
            resp.sendError(SC_NOT_FOUND, "There is no user with such id");
            return false;
        }
        return true;
    }

    public boolean userServiceDeleteRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && userWasFound();
    }

    private String getIdFromUrl() {
        String url = req.getRequestURL().toString();
        return url.substring(url.indexOf(mappingUrl) + mappingUrl.length());
    }

    public void deleteUser() {
        userRepository.delete(userIdFromRequest);
        resp.setStatus(SC_NO_CONTENT);
        makeDeleteUserEvent(userFromRepo);
    }

    private boolean requestUrlContainsId() throws IOException {
        idFromUrl = getIdFromUrl();
        if (idFromUrl.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "Id can't be null");
            return false;
        }
        return true;
    }

    public boolean userServiceUpdateRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && headerNotBlank("username") && userWasFound();
    }

    public User updateUser() {
        userFromRepo = getUserById();
        userFromRepo.setName(req.getHeader("username"));
        User updatedUser = userRepository.update(userFromRepo);
        makeUpdateUserEvent(updatedUser);
        resp.setStatus(SC_OK);
        return updatedUser;
    }
}