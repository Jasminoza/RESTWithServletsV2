package org.yolkin.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

public class FileUploadServlet extends HttpServlet {

    static final int fileMaxSize = 100 * 1024;
    static final int memMaxSize = 100 * 1024;

    private String filePath = "src/main/resources/uploads/";
    private File file;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType("text/html");

            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new File(filePath));
            diskFileItemFactory.setSizeThreshold(memMaxSize);

            ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);
            upload.setSizeMax(fileMaxSize);

            List<FileItem> fileItems = upload.parseRequest(request);

            Iterator<FileItem> iterator = fileItems.iterator();

            writer.println(
                    "<!DOCTYPE html>" +
                    "<html>" +
                        "<head>" +
                            "<title> File Uploading  </title>" +
                        "</head>" +
                            "<body>"
            );

            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                if (!fileItem.isFormField()) {

                    String fileName = fileItem.getName();
                    if (fileName.lastIndexOf("\\") >= 0) {
                        file = new File(filePath +
                                fileName.substring(fileName.lastIndexOf("\\")));
                    } else {
                        file = new File(filePath +
                                fileName.substring(fileName.lastIndexOf("\\") + 1));
                    }
                    fileItem.write(file);

                    writer.println(fileName + " is uploaded.<br>");
                    writer.println("<br/><li><a href=\"/index.jsp\">Go to main page</a></li>");
                    writer.println("<br/><li><a href=\"/FileUpload.html\">Upload new file</a></li>");

                }
            }

            writer.println(
                            "</body>" +
                    "</html>"
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

}