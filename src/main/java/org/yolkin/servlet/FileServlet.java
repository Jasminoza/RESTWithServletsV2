package org.yolkin.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yolkin.model.File;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileServlet extends HttpServlet {
    private FileRepository fileRepository;
    static final int MAX_FILE_SIZE = 100 * 1024;
    static final int MAX_MEMORY_SIZE = 100 * 1024;
    private final String PATH_FOR_UPLOADING = "src/main/webapp/uploads/";

    public void init() {
        fileRepository = new HibernateFileRepositoryImpl();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            StringBuilder stringBuilder = new StringBuilder();
            Long idFromRequest;

            try {
                idFromRequest = Long.valueOf(request.getHeader("file_id"));
            } catch (Exception e) {
                response.setStatus(400);
                stringBuilder.append("Incorrect file id.");
                writer.println(stringBuilder);
                return;
            }

            File file = fileRepository.getById(idFromRequest);

            if (file == null) {
                response.setStatus(400);
                stringBuilder.append("File not found.");
                writer.println(stringBuilder);
                return;
            }

            response.reset();

            try (InputStream in = this.getServletContext().getResourceAsStream("/uploads/" + file.getName());
                 OutputStream out = response.getOutputStream()) {

                response.setHeader("Content-disposition", "attachment; filename = " + file.getName());

                byte[] buffer = new byte[MAX_FILE_SIZE];

                int numBytesRead;

                if (in == null) {
                    response.setStatus(400);
                    stringBuilder.append("File not found on hard drive.");
                    writer.println(stringBuilder);
                    return;
                }

                while ((numBytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, numBytesRead);
                }
            }
        }
    }



    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        try (PrintWriter writer = response.getWriter()) {
            response.setContentType("text/html");

            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new java.io.File(PATH_FOR_UPLOADING));
            diskFileItemFactory.setSizeThreshold(MAX_MEMORY_SIZE);

            ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);
            upload.setSizeMax(MAX_FILE_SIZE);

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
                Date date = new Date();
                if (!fileItem.isFormField()) {

                    java.io.File realFile;

                    String fileName = fileItem.getName();
                    if (fileName.lastIndexOf("\\") >= 0) {
                        realFile = new java.io.File(PATH_FOR_UPLOADING + date + " " +
                                fileName.substring(fileName.lastIndexOf("\\")));
                    } else {
                        realFile = new java.io.File(PATH_FOR_UPLOADING + date + " " +
                                fileName.substring(fileName.lastIndexOf("\\") + 1));
                    }
                    fileItem.write(realFile);

                    File fileAtDB = new File();
                    fileAtDB.setName(date + " " + fileName);
                    fileAtDB.setDateOfUploading(date);
                    fileAtDB = fileRepository.create(fileAtDB);

                    writer.println(fileAtDB.getName() + " is uploaded.<br>");
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
}