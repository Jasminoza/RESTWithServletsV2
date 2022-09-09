package org.yolkin.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yolkin.model.Event;
import org.yolkin.model.File;
import org.yolkin.repository.EventRepository;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateEventRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileUploadServlet extends HttpServlet {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    static final int fileMaxSize = 100 * 1024;
    static final int memMaxSize = 100 * 1024;

    public FileUploadServlet() {
        fileRepository = new HibernateFileRepositoryImpl();
        userRepository = new HibernateUserRepositoryImpl();
        eventRepository = new HibernateEventRepositoryImpl();
    }

    private String filePath = "src/main/resources/uploads/";
    private java.io.File realFile;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType("text/html");

            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRepository(new java.io.File(filePath));
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
                        realFile = new java.io.File(filePath +
                                fileName.substring(fileName.lastIndexOf("\\")));
                    } else {
                        realFile = new java.io.File(filePath +
                                fileName.substring(fileName.lastIndexOf("\\") + 1));
                    }
                    fileItem.write(realFile);

                    File fileAtDB = new File();
                    fileAtDB.setName(fileName);
                    fileAtDB.setDateOfUploading(new Date());
                    fileAtDB = fileRepository.create(fileAtDB);

//                    Long userId = Long.parseLong(request.getHeader("user_id"));
//
//                    Event event = new Event();
//                    event.setFile(fileAtDB);
//                    event.setUsers(userRepository.getById(userId));
//
//                    eventRepository.create(event);

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