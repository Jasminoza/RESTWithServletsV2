package org.yolkin.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yolkin.model.Event;
import org.yolkin.model.File;
import org.yolkin.model.User;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import static javax.servlet.http.HttpServletResponse.*;

public class ServiceHelper {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private Long idFromRequest;
    private Long userIdFromHeader;
    private User userFromRepo;
    private Event eventFromRepo;
    private File fileFromRepo;
    private String mappingUrl;
    private String idFromUrl;
    private String usernameFromHeader;
    private String PATH_FOR_UPLOADING;
    private int MAX_MEMORY_SIZE;
    private int MAX_FILE_SIZE;

    public ServiceHelper(EventRepository eventRepository, HttpServletRequest req, HttpServletResponse resp, String mappingUrl) {
        this.eventRepository = eventRepository;
        this.req = req;
        this.resp = resp;
        this.mappingUrl = mappingUrl;
    }

    public ServiceHelper(EventRepository eventRepository, UserRepository userRepository, HttpServletRequest req, HttpServletResponse resp, String mappingUrl) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.req = req;
        this.resp = resp;
        this.mappingUrl = mappingUrl;
    }

    public ServiceHelper(EventRepository eventRepository, UserRepository userRepository, HttpServletRequest req, HttpServletResponse resp) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.req = req;
        this.resp = resp;
    }

    public ServiceHelper(EventRepository eventRepository, FileRepository fileRepository, UserRepository userRepository, HttpServletRequest req, HttpServletResponse resp, String PATH_FOR_UPLOADING, int MAX_MEMORY_SIZE, int MAX_FILE_SIZE) {
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.req = req;
        this.resp = resp;
        this.PATH_FOR_UPLOADING = PATH_FOR_UPLOADING;
        this.MAX_FILE_SIZE = MAX_FILE_SIZE;
        this.MAX_MEMORY_SIZE = MAX_MEMORY_SIZE;
    }

    public ServiceHelper(EventRepository eventRepository, FileRepository fileRepository, UserRepository userRepository, HttpServletRequest req, HttpServletResponse resp, String PATH_FOR_UPLOADING, int MAX_MEMORY_SIZE, int MAX_FILE_SIZE, String mappingUrl) {
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.req = req;
        this.resp = resp;
        this.PATH_FOR_UPLOADING = PATH_FOR_UPLOADING;
        this.MAX_FILE_SIZE = MAX_FILE_SIZE;
        this.MAX_MEMORY_SIZE = MAX_MEMORY_SIZE;
        this.mappingUrl = mappingUrl;
    }

    public ServiceHelper(EventRepository eventRepository, FileRepository fileRepository, HttpServletRequest req, HttpServletResponse resp, String mappingUrl) {
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
        this.resp = resp;
        this.req = req;
        this.mappingUrl = mappingUrl;
    }

    public ServiceHelper(EventRepository eventRepository, FileRepository fileRepository, UserRepository userRepository, HttpServletRequest req, HttpServletResponse resp, String mappingUrl) {
        this.eventRepository = eventRepository;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.resp = resp;
        this.req = req;
        this.mappingUrl = mappingUrl;
    }

    public boolean userServiceCreateRequestIsCorrect() throws IOException {
        return headerNotBlank("username");
    }

    public User createUser() {
        User user = new User();
        user.setName(usernameFromHeader);
        user = userRepository.create(user);
        makeCreateUserEvent(user);
        resp.setStatus(SC_CREATED);
        return user;
    }

    public boolean userServiceGetByIdRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && userWasFound();
    }

    public User getUserById() {
        return userFromRepo;
    }

    public boolean userServiceUpdateRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && headerNotBlank("username") && userWasFound();
    }

    public User updateUser() {
        userFromRepo.setName(usernameFromHeader);
        User updatedUser = userRepository.update(userFromRepo);
        makeUpdateUserEvent(updatedUser);
        resp.setStatus(SC_OK);
        return updatedUser;
    }

    public boolean userServiceDeleteRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && userWasFound();
    }

    public void deleteUser() {
        userRepository.delete(idFromRequest);
        resp.setStatus(SC_NO_CONTENT);
        makeDeleteUserEvent(userFromRepo);
    }

    public boolean fileServiceCreateRequestIsCorrect() throws IOException {
        return headerNotBlank("user_id") && userFromHeaderWasFound();
    }

    public File createFile() throws IOException {
        ServletFileUpload uploader = setupUploader(PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);
        return saveFileFromRequest(uploader);
    }

    public boolean fileServiceGetByIdRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && fileWasFound();
    }

    public File getFileById() {
        return fileFromRepo;
    }

    public boolean fileServiceUpdateRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && headerNotBlank("user_id") && userFromHeaderWasFound() && fileWasFound();
    }

    public File updateFile() throws IOException {
        ServletFileUpload uploader = setupUploader(PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);
        return updateFileFromRequest(uploader);
    }

    public boolean fileServiceDeleteRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && headerNotBlank("user_id") && userFromHeaderWasFound() && fileWasFound();
    }

    public void deleteFile() {
        resp.setStatus(SC_NO_CONTENT);
        makeDeleteFileEvent(fileFromRepo);
        fileRepository.delete(idFromRequest);
    }

    public boolean eventServiceGetByIdRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && eventWasFound();
    }

    public Event getEventById() {
        return eventFromRepo;
    }

    public boolean eventServiceDeleteRequestIsCorrect() throws IOException {
        return requestUrlContainsId() && idFromUrlIsCorrect(idFromUrl) && eventWasFound();
    }

    public void deleteEvent() {
        resp.setStatus(SC_NO_CONTENT);
        eventRepository.delete(idFromRequest);
    }





    private ServletFileUpload setupUploader(String PATH_FOR_UPLOADING, int MAX_MEMORY_SIZE, int MAX_FILE_SIZE) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(new java.io.File(PATH_FOR_UPLOADING));
        diskFileItemFactory.setSizeThreshold(MAX_MEMORY_SIZE);

        ServletFileUpload uploader = new ServletFileUpload(diskFileItemFactory);
        uploader.setSizeMax(MAX_FILE_SIZE);
        uploader.setFileSizeMax(MAX_MEMORY_SIZE);
        return uploader;
    }

    private java.io.File saveFileFromRequest(FileItem fileItem, String PATH_FOR_UPLOADING, Date date) {
        java.io.File realFile;

        String fileName = fileItem.getName();
        if (fileName.lastIndexOf("\\") >= 0) {
            realFile = new java.io.File(PATH_FOR_UPLOADING + java.io.File.separator + date + " " +
                    fileName.substring(fileName.lastIndexOf("\\")));
        } else {
            realFile = new java.io.File(PATH_FOR_UPLOADING + java.io.File.separator + date + " " +
                    fileName.substring(fileName.lastIndexOf("\\") + 1));
        }

        return realFile;
    }

    private boolean headerNotBlank(String headerName) throws IOException {
        String headerValue = req.getHeader(headerName);

        if (headerValue == null || headerValue.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "Header \"" + headerName + "\" can't be null");
            return false;
        }

        if (headerName.equals("username")) {
            usernameFromHeader = headerValue;
        }

        if (headerName.equals("user_id")) {
           try {
               userIdFromHeader = Long.valueOf(headerValue);
           } catch (NumberFormatException e) {
               resp.sendError(SC_BAD_REQUEST, "user_id is incorrect");
               return false;
           }
        }

        return true;
    }

    private boolean idFromUrlIsCorrect(String idFromUrl) throws IOException {
        try {
            idFromRequest = Long.valueOf(idFromUrl);
            return true;
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Incorrect id");
            return false;
        }
    }

    private boolean userWasFound() throws IOException {
        userFromRepo = userRepository.getById(idFromRequest);

        if (userFromRepo == null) {
            resp.sendError(SC_NOT_FOUND, "There is no user with such id");
            return false;
        }
        return true;
    }

    private boolean userFromHeaderWasFound() throws IOException {
        userFromRepo = userRepository.getById(userIdFromHeader);

        if (userFromRepo == null) {
            resp.sendError(SC_NOT_FOUND, "There is no user with such id");
            return false;
        }
        return true;
    }

    private String getIdFromUrl() {
        String url = req.getRequestURL().toString();
        return url.substring(url.indexOf(mappingUrl) + mappingUrl.length());
    }

    private boolean requestUrlContainsId() throws IOException {
        idFromUrl = getIdFromUrl();
        if (idFromUrl.isBlank()) {
            resp.sendError(SC_BAD_REQUEST, "Id can't be null");
            return false;
        }
        return true;
    }

    private boolean eventWasFound() throws IOException {
        eventFromRepo = eventRepository.getById(idFromRequest);
        if (eventFromRepo == null) {
            resp.sendError(SC_NOT_FOUND, "There is no event with such id");
            return false;
        }
        return true;
    }

    private boolean fileWasFound() throws IOException {
        fileFromRepo = fileRepository.getById(idFromRequest);
        if (fileFromRepo == null) {
            resp.sendError(SC_NOT_FOUND, "There is no file with such id");
            return false;
        }
        return true;
    }

    private File saveFileFromRequest(ServletFileUpload uploader) throws IOException {
        File file = null;
        try {
            Iterator<FileItem> iterator = uploader.parseRequest(req).iterator();
            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                Date date = new Date();
                if (!fileItem.isFormField()) {
                    java.io.File realFile = saveFileFromRequest(fileItem, PATH_FOR_UPLOADING, date);
                    file = saveNewFileToHDD(realFile, fileItem, date);
                }
            }
        } catch (FileUploadException e) {
            resp.sendError(SC_NOT_ACCEPTABLE, "Can't upload file or size of all files exceeds " + MAX_FILE_SIZE / 1024 + " kb.");
            return null;
        }
        return file;
    }

    private File saveNewFileToHDD(java.io.File realFile, FileItem fileItem, Date date) throws IOException {
        File file;
        try {
            File fileForDB = new File();
            fileForDB.setName(realFile.getName());
            fileForDB.setDateOfUploading(date);
            fileForDB.setFilepath(realFile.getPath());

            java.io.File uploadingDirectory = new java.io.File(PATH_FOR_UPLOADING);
            if (!uploadingDirectory.exists()) {
                uploadingDirectory.mkdir();
            }
            fileItem.write(realFile);
            file = fileRepository.create(fileForDB);
            makeCreateFileEvent(file);

            resp.setStatus(SC_CREATED);
        } catch (Exception e) {
            resp.sendError(SC_NOT_IMPLEMENTED, "Can't save file on hard drive");
            return null;
        }
        return file;
    }

    private File updateFileFromRequest(ServletFileUpload uploader) throws IOException {
        File file = null;
        try {
            Iterator<FileItem> iterator = uploader.parseRequest(req).iterator();
            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                Date date = new Date();
                if (!fileItem.isFormField()) {
                    java.io.File realFile = saveFileFromRequest(fileItem, PATH_FOR_UPLOADING, date);
                    file = updateFileOnHDD(realFile, fileItem, date);
                }
            }
        } catch (FileUploadException e) {
            resp.sendError(SC_NOT_ACCEPTABLE, "Can't upload file or size of all files exceeds " + MAX_FILE_SIZE / 1024 + " kb.");
            return null;
        }
        return file;
    }

    private File updateFileOnHDD(java.io.File realFile, FileItem fileItem, Date date) throws IOException {
        File file;
        try {
            File fileForDB = fileFromRepo;
            fileForDB.setName(realFile.getName());
            fileForDB.setDateOfUploading(date);
            fileForDB.setFilepath(realFile.getPath());

            fileItem.write(realFile);
            file = fileRepository.update(fileForDB);
            makeUpdateFileEvent(file);

            resp.setStatus(SC_OK);
        } catch (Exception e) {
            resp.sendError(SC_NOT_IMPLEMENTED, "Can't save file on hard drive");
            return null;
        }
        return file;
    }

    private void makeCreateFileEvent(File file) {
        Event event = new Event();
        User user = userRepository.getById(userIdFromHeader);
        event.setEvent("[" + new Date() + "] " + "INFO: User{id: " + user.getId() + " name: " + user.getName() + "} created " + "File{id: " + file.getId() + " name: " + file.getName() + " filepath: " + file.getFilepath() + " date of uploading: " + file.getDateOfUploading() + "}");
        eventRepository.create(event);
    }

    private void makeUpdateFileEvent(File file) {
        Event event = new Event();
        User user = userRepository.getById(userIdFromHeader);
        event.setEvent("[" + new Date() + "] " + "INFO: User{id: " + user.getId() + " name: " + user.getName() + "} updated " + "File{id: " + file.getId() + " name: " + file.getName() + " filepath: " + file.getFilepath() + " date of uploading: " + file.getDateOfUploading() + "}");
        eventRepository.create(event);
    }

    private void makeDeleteFileEvent(File file) {
        Event event = new Event();
        User user = userRepository.getById(userIdFromHeader);
        event.setEvent("[" + new Date() + "] " + "INFO: User{id: " + user.getId() + " name: " + user.getName() + "} deleted " + "File{id: " + file.getId() + " name: " + file.getName() + " filepath: " + file.getFilepath() + " date of uploading: " + file.getDateOfUploading() + "}");
        eventRepository.create(event);
    }

    private void makeCreateUserEvent(User user) {
        Event event = new Event();
        event.setEvent("[" + new Date() + "] " + "INFO:  User{id: " + user.getId() + " name: " + user.getName() + "} has been created.");
        eventRepository.create(event);
    }

    private void makeUpdateUserEvent(User user) {
        Event event = new Event();
        event.setEvent("[" + new Date() + "] " + "INFO:  User{id: " + user.getId() + " name: " + user.getName() + "} has been updated.");
        eventRepository.create(event);
    }

    private void makeDeleteUserEvent(User user) {
        Event event = new Event();
        event.setEvent("[" + new Date() + "] " + "INFO:  User{id: " + user.getId() + " name: " + user.getName() + "} has been deleted.");
        eventRepository.create(event);
    }
}