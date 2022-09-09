package org.yolkin.servlet;

import org.yolkin.model.File;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FilesServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            FileRepository fileRepository = new HibernateFileRepositoryImpl();
            List<File> files = fileRepository.getAll();

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("<!DOCTYPE = html>");
            stringBuilder.append("<html>");
            stringBuilder.append("<head><title>");
            stringBuilder.append("<h1>Files list</h1>");
            stringBuilder.append("</title></head>");

            stringBuilder.append("<body>");
            stringBuilder.append("<h1>FILES DATA</h1>");

            for (File file : files) {
                stringBuilder.append("File ID: " + file.getId());
                stringBuilder.append("<br/>");
                stringBuilder.append("File name: " + file.getName());
                stringBuilder.append("<br/>");
                stringBuilder.append("File date of uploading: " + file.getDateOfUploading());
                stringBuilder.append("<br/>");
                stringBuilder.append("<br/>");
            }

            stringBuilder.append("</body>");
            stringBuilder.append("</html>");

            writer.println(stringBuilder);
        }
    }
}