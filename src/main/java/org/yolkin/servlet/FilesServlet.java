package org.yolkin.servlet;

import org.yolkin.model.File;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;
import org.yolkin.util.ServletHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FilesServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletHelper helper = new ServletHelper(response);

        FileRepository fileRepository = new HibernateFileRepositoryImpl();
        List<File> files = fileRepository.getAll();
        helper.setResponseHead("Files list");

        if (files.size() == 0) {
            helper.addToResponseBody("There is no files in database.");
            helper.sendResponse();
            return;
        }

        helper.addH1ToResponseBody("FILES DATA");

        for (File file : files) {
            helper.addToResponseBody("File ID: " + file.getId());
            helper.addToResponseBody("File name: " + file.getName());
            helper.addToResponseBody("File date of uploading: " + file.getDateOfUploading());
            helper.addToResponseBody("Uploaded user: " + file.getUser().getName());
            helper.addToResponseBody("<br/>");
        }

        helper.sendResponse();
    }
}