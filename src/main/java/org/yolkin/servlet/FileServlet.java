package org.yolkin.servlet;

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

public class FileServlet extends HttpServlet {
    private FileRepository fileRepository;
    private String filePath = "src/main/resources/uploads/";
    private java.io.File realFile;
    static final int fileMaxSize = 100 * 1024;
    static final int memMaxSize = 100 * 1024;

    public void init() {
        fileRepository = new HibernateFileRepositoryImpl();
    }

//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        try (PrintWriter writer = response.getWriter()) {
//            response.setContentType("image/png");
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
//            diskFileItemFactory.setRepository(new java.io.File(filePath));
//            diskFileItemFactory.setSizeThreshold(memMaxSize);
//
//            ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);
//            upload.setSizeMax(fileMaxSize);
//
//            List<FileItem> fileItems = null;
//            try {
//                fileItems = upload.parseRequest(request);
//            } catch (FileUploadException e) {
//                response.setStatus(400);
//                stringBuilder.append("User not found.");
//                writer.println(stringBuilder);
//                return;
//            }
//            stringBuilder.append("<!DOCTYPE = html>");
//            stringBuilder.append("<html>");
//            stringBuilder.append("<head><title>");
//            stringBuilder.append("<h1>User details</h1>");
//            stringBuilder.append("</title></head>");
//
//            Iterator<FileItem> iterator = fileItems.iterator();
//
//            FileItem fileItem = iterator.next();
//            if (!fileItem.isFormField()) {
//                Date date = new Date();
//                String fileName = fileItem.getName();
//                if (fileName.lastIndexOf("\\") >= 0) {
//                    realFile = new java.io.File(filePath +
//                            fileName.substring(fileName.lastIndexOf("\\")));
//                } else {
//                    realFile = new java.io.File(filePath +
//                            fileName.substring(fileName.lastIndexOf("\\") + 1));
//                }
//
//                try {
//                    fileItem.write(realFile);
//
//                    File fileAtDB = new File();
//                    fileAtDB.setName(fileName);
//                    fileAtDB.setDateOfUploading(date);
//                    fileAtDB = fileRepository.create(fileAtDB);
//
//                    stringBuilder.append("<body>");
//                    stringBuilder.append("<h1>File was saved successfully</h1>");
//
//                    stringBuilder.append("File ID: " + fileAtDB.getId());
//                    stringBuilder.append("<br/>");
//                    stringBuilder.append("File name: " + fileAtDB.getName());
//                    stringBuilder.append("<br/>");
//                    stringBuilder.append("File date of uploading: " + fileAtDB.getDateOfUploading());
//                    stringBuilder.append("<br/>");
//                    stringBuilder.append("<br/>");
//                    stringBuilder.append("</body>");
//                    stringBuilder.append("</html>");
//
//                    writer.println(stringBuilder);
//                } catch (Exception e) {
//                    response.setStatus(400);
//                    stringBuilder.append("Can't save file.");
//                    writer.println(stringBuilder);
//                }
//            }
//        }
//    }

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

                byte[] buffer = new byte[fileMaxSize];

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
}