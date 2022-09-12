package org.yolkin.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yolkin.model.File;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;
import org.yolkin.util.ServletHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

public class FileServlet extends HttpServlet {
    private FileRepository fileRepository;
    static final int MAX_FILE_SIZE = 100 * 1024;
    static final int MAX_MEMORY_SIZE = 100 * 1024;
    private final String PATH_FOR_UPLOADING = "src/main/webapp/uploads";

    public void init() {
        fileRepository = new HibernateFileRepositoryImpl();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletHelper helper = new ServletHelper(response);

        Long idFromRequest;

        try {
            idFromRequest = Long.valueOf(request.getHeader("file_id"));
        } catch (Exception e) {
            helper.sendBadRequestStatus("Incorrect file id.");
            return;
        }

        File file = fileRepository.getById(idFromRequest);

        if (file == null) {
            helper.sendBadRequestStatus("There is no file with such id.");
            return;
        }

        try (InputStream streamFromHardDrive = this.getServletContext().getResourceAsStream(PATH_FOR_UPLOADING.substring(PATH_FOR_UPLOADING.lastIndexOf("/")) + java.io.File.separator + file.getName())) {

            response.setHeader("Content-disposition", "attachment; filename = " + file.getName());

            if (streamFromHardDrive == null) {
                helper.sendBadRequestStatus("File not found on hard drive.");
                return;
            }

            try {
                helper.sendResponseWithFile(streamFromHardDrive, MAX_FILE_SIZE);
            } catch (IOException e) {
                helper.sendBadRequestStatus("Can's send file. Please, contact administrator.");
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletHelper helper = new ServletHelper(response);

        ServletFileUpload uploader = helper.setupUploader(PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);

        try {
            Iterator<FileItem> iterator = uploader.parseRequest(request).iterator();
            helper.setResponseHead("File details");

            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                Date date = new Date();
                if (!fileItem.isFormField()) {

                    java.io.File realFile = helper.getFileFromRequest(fileItem, PATH_FOR_UPLOADING, date);

                    try {
                        fileItem.write(realFile);
                        File fileAtDB = new File();
                        fileAtDB.setName(realFile.getName());
                        fileAtDB.setDateOfUploading(date);
                        fileAtDB = fileRepository.create(fileAtDB);

                        helper.setResponseBody("File " + fileAtDB.getName() + " was saved successfully.");
                        helper.closeResponseBodyTag();
                        helper.closeResponseTag();
                        helper.sendResponse();
                    } catch (Exception e) {
                        helper.sendBadRequestStatus("Can't save file on hard drive.");
                    }
                }
            }
        } catch (FileUploadException e) {
            helper.sendBadRequestStatus("Wrong request.");
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        Long idFromRequest;

        try {
            idFromRequest = Long.valueOf(request.getHeader("file_id"));
        } catch (Exception e) {
            helper.sendBadRequestStatus("Incorrect file id.");
            return;
        }

        File file = fileRepository.getById(idFromRequest);

        if (file == null) {
            helper.sendBadRequestStatus("File not found.");
            return;
        }

        helper.setResponseHead("File details");

        java.io.File fileToDeletion = new java.io.File(PATH_FOR_UPLOADING + java.io.File.separator + file.getName());
        fileToDeletion.delete();

        fileRepository.delete(idFromRequest);

        helper.setResponseBody("File " + file.getName() + " was removed successfully.");
        helper.closeResponseBodyTag();
        helper.closeResponseTag();
        helper.sendResponse();
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        Long idFromRequest;

        try {
            idFromRequest = Long.valueOf(request.getHeader("file_id"));
        } catch (Exception e) {
            helper.sendBadRequestStatus("Incorrect file id.");
            return;
        }

        File file = fileRepository.getById(idFromRequest);

        if (file == null) {
            helper.sendBadRequestStatus("File not found.");
            return;
        }

        ServletFileUpload uploader = helper.setupUploader(PATH_FOR_UPLOADING, MAX_MEMORY_SIZE, MAX_FILE_SIZE);

        try {
            Iterator<FileItem> iterator = uploader.parseRequest(request).iterator();
            helper.setResponseHead("File details");

            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                Date date = new Date();
                if (!fileItem.isFormField()) {

                    java.io.File realFile = helper.getFileFromRequest(fileItem, PATH_FOR_UPLOADING, date);

                    try {
                        java.io.File oldFile = new java.io.File(PATH_FOR_UPLOADING + java.io.File.separator + file.getName());

                        if (!oldFile.delete()) {
                            helper.sendBadRequestStatus("Can't update file on hard drive.");
                            return;
                        } else {
                            fileItem.write(realFile);
                            file.setName(realFile.getName());
                            file.setDateOfUploading(date);
                            file = fileRepository.update(file);

                            helper.setResponseBody("File " + file.getName() + " was updated successfully.");
                            helper.closeResponseBodyTag();
                            helper.closeResponseTag();
                            helper.sendResponse();
                        }
                    } catch (Exception e) {
                        helper.sendBadRequestStatus("Can't save file on hard drive.");
                    }
                }
            }
        } catch (FileUploadException e) {
            helper.sendBadRequestStatus("Wrong request.");
        }
    }
}