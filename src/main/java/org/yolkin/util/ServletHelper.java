package org.yolkin.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class ServletHelper {

    private StringBuilder stringBuilder = new StringBuilder();
    private HttpServletResponse response;

    public ServletHelper(HttpServletResponse response) {
        this.response = response;
    }

    public void sendBadRequestStatus(String cause) throws IOException {
        response.setStatus(400);
        stringBuilder.append(cause);
        response.getWriter().println(stringBuilder);
    }

    public void sendResponseWithFile(InputStream streamFromHardDrive, int MAX_FILE_SIZE) throws IOException {
        byte[] buffer = new byte[MAX_FILE_SIZE];

        int numBytesRead;
        while ((numBytesRead = streamFromHardDrive.read(buffer)) > 0) {
            response.getOutputStream().write(buffer, 0, numBytesRead);
        }
    }

    public ServletFileUpload setupUploader(String PATH_FOR_UPLOADING, int MAX_MEMORY_SIZE, int MAX_FILE_SIZE) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(new java.io.File(PATH_FOR_UPLOADING));
        diskFileItemFactory.setSizeThreshold(MAX_MEMORY_SIZE);

        ServletFileUpload uploader = new ServletFileUpload(diskFileItemFactory);
        uploader.setSizeMax(MAX_FILE_SIZE);
        return uploader;
    }

    public void setResponseHead(String title) {
        stringBuilder.append("<!DOCTYPE = html>");
        stringBuilder.append("<html>");
        stringBuilder.append("<head><title>");
        stringBuilder.append("<h1>" + title + "</h1>");
        stringBuilder.append("</title></head>");
        stringBuilder.append("<body>");
    }

    public void addH1ToResponseBody(String text) {
        stringBuilder.append("<h1> " + text + "</h1>");
    }

    public void addToResponseBody(String text) {
        stringBuilder.append(text);
        stringBuilder.append("<br/>");
    }

    private void closeResponseBodyTag() {
        stringBuilder.append("<br/><li><a href=\"/index.jsp\">Go to main page</a></li>");
        stringBuilder.append("<br/><li><a href=\"/FileUpload.html\">Upload new file</a></li>");
        stringBuilder.append("<br/>");
        stringBuilder.append("</body>");
    }

    private void closeResponseTag() {
        stringBuilder.append("</html>");
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

    public void sendResponse() throws IOException {
        stringBuilder.append("<br/>");
        closeResponseBodyTag();
        closeResponseTag();
        response.getWriter().println(stringBuilder);
    }
}