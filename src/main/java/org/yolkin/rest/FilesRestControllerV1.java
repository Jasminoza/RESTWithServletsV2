package org.yolkin.rest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yolkin.dto.FileCreationDTO;
import org.yolkin.dto.FileDTO;
import org.yolkin.dto.UserDTO;
import org.yolkin.dto.mapper.UserMapper;
import org.yolkin.model.UserEntity;
import org.yolkin.service.FileService;
import org.yolkin.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import static javax.servlet.http.HttpServletResponse.*;
import static org.yolkin.util.HttpUtil.*;

public class FilesRestControllerV1 extends HttpServlet {
    private final FileService fileService;
    private final UserService userService;
    private final String mappingUrl = "/api/v1/files/";
    String PATH_FOR_UPLOADING = "src/main/webapp/uploads";
    int MAX_MEMORY_SIZE = 100 * 1024;
    int MAX_FILE_SIZE = 100 * 1024;

    public FilesRestControllerV1() {
        this.fileService = new FileService();
        this.userService = new UserService();
    }

    public FilesRestControllerV1(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf(mappingUrl) + mappingUrl.length());

        if (id.isBlank()) {
            sendJsonFrom(resp, fileService.getAll());
        } else {
            if (idFromUrlIsCorrect(id, resp)) {
                FileDTO fileDTO = fileService.getById(Long.valueOf(id));
                if (fileDTO == null) {
                    resp.sendError(SC_NOT_FOUND, "There is no File with such id");
                }  else {
                    sendJsonFrom(resp, fileDTO);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String headerName = "user_id";

        if (headerIsNotBlank(headerName, req, resp)) {
            if (headerUserIdIsCorrect(req, resp)) {
                Long userIdFromHeader = Long.valueOf(req.getHeader(headerName));
                UserDTO userDTO = userService.getById(userIdFromHeader);

                if (userDTO == null) {
                    resp.sendError(SC_NOT_FOUND, "There is no User with such id");
                    return;
                }

                ServletFileUpload uploader = setupUploader(PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);
                FileCreationDTO fileCreationDTO = getFileEntityFromRequest(uploader, req, resp);

                if (fileCreationDTO == null) {
                    return;
                }

                UserEntity user = UserMapper.toUser(userDTO);
                fileCreationDTO.setUser(user);

                FileDTO fileDTO = fileService.create(fileCreationDTO);

                if (fileDTO == null) {
                    resp.sendError(SC_NOT_IMPLEMENTED, "Can't save file");
                    return;
                }

                sendJsonFrom(resp, fileDTO);
                resp.setStatus(SC_CREATED);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        HttpUtil helper = new HttpUtil(resp);
//        helper.sendJsonFrom(fileService.update(req, resp, mappingUrl));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        fileService.delete(req, resp, mappingUrl);
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

    private FileCreationDTO getFileEntityFromRequest(ServletFileUpload uploader, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FileCreationDTO fileCreationDTO = new FileCreationDTO();
        try {
            Iterator<FileItem> iterator = uploader.parseRequest(req).iterator();
            if (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                String fileName = fileItem.getName();
                String filePath = getFilepathFromFile(fileName);

                fileCreationDTO.setName(fileName);
                fileCreationDTO.setFilePath(filePath);
                fileCreationDTO.setFileItem(fileItem);
                fileCreationDTO.setPathForUploading(PATH_FOR_UPLOADING);
            }
        } catch (FileUploadException e) {
            resp.sendError(SC_NOT_ACCEPTABLE, "Can't upload file or size of all files exceeds " + MAX_FILE_SIZE / 1024 + " kb.");
            return null;
        }
        return fileCreationDTO;
    }

    private String getFilepathFromFile(String fileName) {
        if (fileName.lastIndexOf("\\") >= 0) {
            return PATH_FOR_UPLOADING + java.io.File.separator + new Date() + " " +
                    fileName.substring(fileName.lastIndexOf("\\"));
        } else {
            return PATH_FOR_UPLOADING + java.io.File.separator + new Date() + " " +
                    fileName.substring(fileName.lastIndexOf("\\") + 1);
        }
    }
}
