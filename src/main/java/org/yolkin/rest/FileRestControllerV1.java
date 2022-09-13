package org.yolkin.rest;

import org.yolkin.model.File;
import org.yolkin.service.FileService;
import org.yolkin.util.GsonHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileRestControllerV1 extends HttpServlet {

    private final FileService fileService;

    public FileRestControllerV1() {
        fileService = new FileService();
    }

    public FileRestControllerV1(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GsonHelper helper = new GsonHelper(resp);

        String url = req.getRequestURL().toString();
        String id = url.substring(url.indexOf("/files") + 6);

        if (id.isBlank()) {
            helper.sendJsonFrom(fileService.getAll());
        } else {
            Long idFromRequest;

            try {
                idFromRequest = Long.valueOf(id);
            } catch (Exception e) {
                resp.sendError(400, "Incorrect file id.");
                return;
            }

            File file = fileService.getById(idFromRequest);
            helper.sendJsonFrom(file);
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
