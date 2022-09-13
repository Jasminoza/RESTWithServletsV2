package org.yolkin.rest;

import com.google.gson.Gson;
import org.yolkin.model.File;
import org.yolkin.repository.FileRepository;
import org.yolkin.repository.hibernate.HibernateFileRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FileRestControllerV1 extends HttpServlet {

    private final FileRepository fileRepository;
    private final Gson GSON = new Gson();

    public FileRestControllerV1() {
        fileRepository = new HibernateFileRepositoryImpl();
    }

    public FileRestControllerV1(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURL().toString().contains()) {
            List<File> files = fileRepository.getAll();
            resp.getWriter().write(GSON.toJson(files));
        } else {
            File file = fileRepository.getById(1L);
            resp.getWriter().write(GSON.toJson(file));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
